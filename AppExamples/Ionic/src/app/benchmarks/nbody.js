async function nbody(number, writer) {
  var PI = 3.141592653589793;
  var SOLAR_MASS = 4 * PI * PI;
  var DAYS_PER_YEAR = 365.24;

  function Body(x, y, z, vx, vy, vz, mass) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.vx = vx;
    this.vy = vy;
    this.vz = vz;
    this.mass = mass;
  }

  Body.prototype.offsetMomentum = function (px, py, pz) {
    this.vx = -px / SOLAR_MASS;
    this.vy = -py / SOLAR_MASS;
    this.vz = -pz / SOLAR_MASS;
    return this;
  };

  function Jupiter() {
    return new Body(
      4.8414314424647209,
      -1.16032004402742839,
      -1.03622044471123109e-1,
      1.66007664274403694e-3 * DAYS_PER_YEAR,
      7.69901118419740425e-3 * DAYS_PER_YEAR,
      -6.90460016972063023e-5 * DAYS_PER_YEAR,
      9.54791938424326609e-4 * SOLAR_MASS,
    );
  }

  function Saturn() {
    return new Body(
      8.34336671824457987,
      4.12479856412430479,
      -4.03523417114321381e-1,
      -2.76742510726862411e-3 * DAYS_PER_YEAR,
      4.99852801234917238e-3 * DAYS_PER_YEAR,
      2.30417297573763929e-5 * DAYS_PER_YEAR,
      2.85885980666130812e-4 * SOLAR_MASS,
    );
  }

  function Uranus() {
    return new Body(
      1.2894369562139131e1,
      -1.51111514016986312e1,
      -2.23307578892655734e-1,
      2.96460137564761618e-3 * DAYS_PER_YEAR,
      2.3784717395948095e-3 * DAYS_PER_YEAR,
      -2.96589568540237556e-5 * DAYS_PER_YEAR,
      4.36624404335156298e-5 * SOLAR_MASS,
    );
  }

  function Neptune() {
    return new Body(
      1.53796971148509165e1,
      -2.59193146099879641e1,
      1.79258772950371181e-1,
      2.68067772490389322e-3 * DAYS_PER_YEAR,
      1.62824170038242295e-3 * DAYS_PER_YEAR,
      -9.5159225451971587e-5 * DAYS_PER_YEAR,
      5.15138902046611451e-5 * SOLAR_MASS,
    );
  }

  function Sun() {
    return new Body(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, SOLAR_MASS);
  }

  function NBodySystem(bodies) {
    this.bodies = bodies;
    var px = 0.0;
    var py = 0.0;
    var pz = 0.0;
    var size = this.bodies.length;
    for (var i = 0; i < size; i++) {
      var b = this.bodies[i];
      var m = b.mass;
      px += b.vx * m;
      py += b.vy * m;
      pz += b.vz * m;
    }
    this.bodies[0].offsetMomentum(px, py, pz);
  }

  NBodySystem.prototype.advance = function (dt) {
    var dx, dy, dz, distance, mag;
    var size = this.bodies.length;

    for (var i = 0; i < size; i++) {
      var bodyi = this.bodies[i];
      for (var j = i + 1; j < size; j++) {
        var bodyj = this.bodies[j];
        dx = bodyi.x - bodyj.x;
        dy = bodyi.y - bodyj.y;
        dz = bodyi.z - bodyj.z;

        distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        mag = dt / (distance * distance * distance);

        bodyi.vx -= dx * bodyj.mass * mag;
        bodyi.vy -= dy * bodyj.mass * mag;
        bodyi.vz -= dz * bodyj.mass * mag;

        bodyj.vx += dx * bodyi.mass * mag;
        bodyj.vy += dy * bodyi.mass * mag;
        bodyj.vz += dz * bodyi.mass * mag;
      }
    }

    for (var i = 0; i < size; i++) {
      var body = this.bodies[i];
      body.x += dt * body.vx;
      body.y += dt * body.vy;
      body.z += dt * body.vz;
    }
  };

  NBodySystem.prototype.energy = function () {
    var dx, dy, dz, distance;
    var e = 0.0;
    var size = this.bodies.length;

    for (var i = 0; i < size; i++) {
      var bodyi = this.bodies[i];

      e +=
        0.5 *
        bodyi.mass *
        (bodyi.vx * bodyi.vx + bodyi.vy * bodyi.vy + bodyi.vz * bodyi.vz);

      for (var j = i + 1; j < size; j++) {
        var bodyj = this.bodies[j];
        dx = bodyi.x - bodyj.x;
        dy = bodyi.y - bodyj.y;
        dz = bodyi.z - bodyj.z;

        distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        e -= (bodyi.mass * bodyj.mass) / distance;
      }
    }
    return e;
  };

  async function executeNbody(n, writer) {
    const output = [];
    var bodies = new NBodySystem(
      Array(Sun(), Jupiter(), Saturn(), Uranus(), Neptune()),
    );
    output.push(bodies.energy().toFixed(9));
    for (var i = 0; i < n; i++) {
      bodies.advance(0.01);
    }
    output.push(bodies.energy().toFixed(9));
    await writer(output.join("\n"))
  }

  await executeNbody(number, writer);
}

export default nbody;
