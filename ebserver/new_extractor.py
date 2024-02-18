import re
import os
from numpy import NaN

def readExperimentResult(exp_rel_path, executionNumber, readExecutionData, start_at = 1):
    benchmarks = []
    experiment_path = exp_rel_path
    for framework in os.scandir(experiment_path):
        for device in os.scandir(framework.path):
            if (device.is_dir() == False): 
                continue
            for benchmark in os.scandir(device.path):
                executon_data = readExecutionData(executionNumber, framework.name, device.name, benchmark.name, benchmark.path, start_at)
                for execution_datum in executon_data:
                    benchmarks.append(execution_datum)
    return benchmarks

# Memory file methods
# values in MB
def get_memory(filename):
    try:
        with open(filename, 'r') as file:
            for line in file:
                if 'TOTAL' in line:
                    tokens = line.split()

                    memory = float(tokens[1]) / 1000
                    heap_size, heap_alloc, heap_free = list(map(lambda x: float(x) / 1000, tokens[-3:]))

                    return memory, heap_free, heap_alloc, heap_size
    except Exception as e:
        print(f"Error on {filename}: {e}")    
    return [NaN] * 4

# Data file methods
# values in MB
def get_data(filename, package):
    try:
        with open(filename, 'r') as file:
            for line in file:
                if package not in line:
                    continue

                line = next(file)

                # if '(Cached)' in line or line.strip() == 'TOTAL: 100%' or '/' not in line:
                if '(Cached)' in line or '/' not in line:
                    return None, None, None

                # TOTAL: 100% (11MB-11MB-11MB/2,6MB-2,6MB-2,6MB/73MB-73MB-73MB over 1)
                start_idx = line.find('(') + 1
                end_idx = line.find('/', start_idx)

                # mem_data_low, mem_data_med, mem_data_high
                # 11MB-11MB-11MB ==> [11.0, 11.0, 11.0]
                return list(map(lambda x: float(x[:-2]), line[start_idx:end_idx].split('-')))
    except Exception as e:
        print(f"Error on {filename}: {e}")
    return [NaN, NaN, NaN]


# Energy file methods
def get_voltage(filename):
    volt_keyword = 'volt='
    try:
        with open(filename, 'r') as file:
            for line in file:
                if volt_keyword in line:
                    start = line.find(volt_keyword) + len(volt_keyword)
                    end = line.find(' ', start)
                    return float(line[start:end]) * 1e-3
    except Exception as e:
        print(f"Error on {filename}: {e}")
    return NaN

def get_appid(filename):
    try:
        with open(filename, 'r') as file:
            for line in file:
                if "top=" in line:
                    start = line.find('=') + 1
                    end = line.find(':', start)
                    return line[start:end]
    except Exception as e:
        print(f"Error on {filename}: {e}")
            
def get_package(filename):
    with open(filename, 'r') as file:
        for line in file:
            if "top=" in line:
                top = line.find("top=")
                start = line.find('"', top) + 1
                end = line.find('"', start)
                return line[start:end]

# values in joules
def get_energy(filename, app_id, voltage):
    with open(filename, 'r') as file:
        for line in file:
            if f'uid {app_id}' in line.lower():
                start = line.find(': ') + 2
                end = line.find(' ', start)
                power = float(line[start:end])
                return power * 3.6 * voltage
    return NaN

# values in seconds
def get_time(filename, app_id):
    try:
        found = 0

        foreground_time, cpu_time = NaN, NaN

        with open(filename, 'r') as file:
            for line in file:
                if found == 3:
                    return foreground_time, cpu_time

                if app_id in line and '=' not in line:
                    found = 1
                elif 'Foreground activities:' in line and found:
                    time_str = substring(line, ':', 'r')
                    foreground_time = time_str2int(time_str)
                    found = 2
                elif 'Total cpu time' in line and found:
                    time_str = substring(line, 'u=', 's=')
                    cpu_time = time_str2int(time_str)
                    found = 3

        return [foreground_time, cpu_time]
    except Exception as e:
        print(f"Error on {filename}: {e}")


def substring(str, start_str, end_str):
    start_idx = str.find(start_str) + len(start_str)
    end_idx = str.find(end_str, start_idx)
    return str[start_idx:end_idx]

def time_str2int(time_str):
    # match = re.search(r'\d+(?:s \d+ms|ms)', time_str)
    # if not match:
    #     return 0

    # time = match.group()
    # if 's ' in time:
    #     seconds, miliseconds = time.split('s ')
    # else:
    #     seconds, miliseconds = 0, time

    # seconds = int(seconds)
    # miliseconds = int(miliseconds[:-2])

    # return float(seconds * 1000 + miliseconds) / 1000


    total_seconds = 0
    
    minutes = re.search(r'(\d+)m ', time_str)
    if (minutes):
        total_seconds += float(minutes.group(1)) * 60
    
    seconds = re.search(r'(\d+)s', time_str)
    if (seconds):
        total_seconds += float(seconds.group(1))

    miliseconds = re.search(r'(\d+)ms', time_str)
    if (miliseconds):
        total_seconds += float(miliseconds.group(1)) / 1000

    return total_seconds