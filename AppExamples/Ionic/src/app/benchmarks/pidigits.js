async function pidigits(number, writer) {
  const n = number;

  const char0 = '0'.charCodeAt(0);
  const charT = '\t'.charCodeAt(0);
  const charN = '\n'.charCodeAt(0);
  const charC = ':'.charCodeAt(0);

  let bufSize = (10 + 2 + n.toString().length + 1) * (n / 10);
  for (let i = 10, ii = 10 ** (Math.log10(n) >>> 0); i < ii; i *= 10) {
    bufSize -= i - 1;
  }
  //const buf = Buffer.allocUnsafe(bufSize);
  const buf = new Uint8Array(bufSize);
  let bufOffs = 0;

  let i = 0;
  let k = 0n;
  let k2 = 1n;
  let acc = BigInt(0);
  let den = BigInt(1);
  let num = BigInt(1);
  let tmp = BigInt(0);
  let d3 = BigInt(0);
  let d4 = BigInt(0);

  while (i < n) {
    k++;
    k2 += 2n;

    acc = acc + num * 2n; //BigInt.addMul(acc, num, 2);
    acc = acc * k2; //BigInt.mul(acc, acc, k2);
    den = den * k2; //BigInt.mul(den, den, k2);
    num = num * k; // BigInt.mul(num, num, k);

    if (num > acc) {
      continue;
    }

    tmp = num * 3n; //BigInt.mul(tmp, num, 3);
    tmp = tmp + acc; //BigInt.add(tmp, tmp, acc);
    d3 = tmp / den; //BigInt.div(d3, tmp, den);

    tmp = tmp + num; //BigInt.add(tmp, tmp, num);
    d4 = tmp / den; //BigInt.div(d4, tmp, den);

    if (d3 != d4) {
      //attention
      continue;
    }

    const d = Number(d3);

    buf.set([d + char0], bufOffs++);
    //buf.writeInt8(d + char0, bufOffs++);
    i++;
    if (i % 10 === 0) {
      writeLineEnd(i);
    }

    acc = acc - den * BigInt(d); //BigInt.subMul(acc, den, d);
    acc = acc * 10n; //BigInt.mul(acc, acc, 10);
    num = num * 10n; //BigInt.mul(num, num, 10);
  }

  function writeLineEnd(iParam) {
    //        buf.writeInt8(charT, bufOffs++);
    //      buf.writeInt8(charC, bufOffs++);

    buf.set([charT], bufOffs++);
    buf.set([charC], bufOffs++);

    let str = iParam.toString();
    for (let index = 0; index < str.length; index++) {
      buf.set([str.charCodeAt(index)], bufOffs++);
    }
    //buf.write(str, bufOffs, bufOffs += str.length);
    //buf.writeInt8(charN, bufOffs++);
    buf.set([charN], bufOffs++);
  }
  await writer(convertToString(buf));
}

function convertToString(array) {
  return String.fromCharCode.apply(null, array);
}

export default pidigits;
