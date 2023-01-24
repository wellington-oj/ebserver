import 'package:http/http.dart' as http;
import 'package:device_info_plus/device_info_plus.dart';

const BASE_URL = "http://192.168.1.68:3000/";
// change the ip above to the ebserver machine ip
const START_ENDPOINT = "what_now/";
const LOGDATA_ENDPOINT = "logdata/";
const FINISH_ENDPOINT = "done/";

const APP_ID = "com.example.starter";
const FRAMEWORK = "FLUTTER";
const TEST_TYPE = "benchmarkGame";

class ProgramExecution {
  late String program;
  late String parameter;

  ProgramExecution (this.program, this.parameter);
}

Future<ProgramExecution> start() async {
  AndroidDeviceInfo androidInfo = await DeviceInfoPlugin().androidInfo;
  var response = await http.get(
      Uri.parse(BASE_URL + START_ENDPOINT),
      headers: {
        'device': androidInfo.model!,
        'test_type': TEST_TYPE
      }
  );
  if (response.statusCode == 200){
    final splited = response.body.toString().split("-");
    return ProgramExecution(splited[0], splited[1]);
  }
  throw Exception("Error on starting code");
}

Future<void> logdata() async {
  AndroidDeviceInfo androidInfo = await DeviceInfoPlugin().androidInfo;
  var response = await http.get(
      Uri.parse(BASE_URL + LOGDATA_ENDPOINT),
      headers: {
        'device': androidInfo.model!,
        'test_type': TEST_TYPE,
        'application_id': APP_ID,
        'framework': FRAMEWORK
      }
  );
  if (response.statusCode != 200){
    throw Exception("Error on logging battery status code");
  }
}

void done() async {
  AndroidDeviceInfo androidInfo = await DeviceInfoPlugin().androidInfo;
  http.get(
      Uri.parse(BASE_URL + FINISH_ENDPOINT),
      headers: {
        'device': androidInfo.model!,
        'test_type': TEST_TYPE,
        'application_id': APP_ID,
        'activity': '.MainActivity'
      }
  );
}
