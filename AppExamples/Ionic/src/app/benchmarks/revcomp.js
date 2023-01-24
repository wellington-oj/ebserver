async function revcomp(data, writer) {
  const output = []

  const READ_SIZE = 16000;
  const LA_LEN = 30;
  const headLA = new LinkedArray();
  let i = 0;
  let la = headLA;
  let lnIdx = 0;
  let lines = la.data;

  var nullLoop = [41, 45, 41, 45, 41, 41, 45, 41, 45, 41, 41, 45];
  let counter = 0;
  var nullNumber = 44;
  var indxNullLoop = 0;

  let needHeader = true;
  let headerPartial = '';
  let isFirst = true;
  let isFinal = false;
  // const writeBuffer = Buffer.allocUnsafe(READ_SIZE + READ_SIZE / 61 | 0);
  // let buf = Buffer.from(data);
  const writeBuffer = new Uint8Array((READ_SIZE + READ_SIZE / 61) | 0);
  let buf = new TextEncoder('utf-8').encode(data);


  const decoder = new TextDecoder();

  let metaI;
  let numLines;

  await exec(writer);

  function getChunk() {
    while (buf.length) {
      i = READ_SIZE > buf.length ? (i = buf.length - 1) : (i = READ_SIZE);
      let ret = buf.slice(0, i);
      buf = buf.slice(i);
      return ret;
    }
  }

  async function exec(writer) {
    while (!isFinal) {
      read();
    }
    await writer(output.join("\n"))
  }

  function LinkedArray(prev) {
    this.prev = prev;
    this.next = undefined;
    this.pos = 0;
    this.data = [];
  }

  function getCompChar(code) {
    switch (code) {
      case 65:
      case 97:
        return 84;
      case 66:
      case 98:
        return 86;
      case 67:
      case 99:
        return 71;
      case 68:
      case 100:
        return 72;
      case 71:
      case 103:
        return 67;
      case 72:
      case 104:
        return 68;
      case 75:
      case 107:
        return 77;
      case 77:
      case 109:
        return 75;
      case 78:
      case 110:
        return 78;
      case 82:
      case 114:
        return 89;
      case 83:
      case 115:
        return 83;
      case 84:
      case 85:
        return 65;
      case 86:
      case 118:
        return 66;
      case 87:
      case 119:
        return 87;
      case 116:
      case 117:
        return 65;
      case 89:
      case 121:
        return 82;
    }
  }

  function reverseCompPrint(line) {
    let _metaI = metaI;
    let _numLines = numLines;
    let count = 0;
    const target = writeBuffer;
    const len = line.length;
    const right = line.length - 1;

    let ileft = 0;
    let iright = 0;

    let c;
    while (iright < len) {
      c = line[right - iright];
      iright++;
      if (c === 10) {
        // skip linebreaks
        if (iright === len) {
          break;
        }
        c = line[right - iright];
        iright++;
      }
      target[ileft] = getCompChar(c);
      ileft++;
      count++;
      if ((count + _metaI - _numLines) % 60 === 0) {
        // need a linebreak
        target[ileft] = 10;
        ileft++;
        count++;
        _numLines++;
      }
    }
    metaI = _metaI + count;
    numLines = _numLines;

    output.push(decoder.decode(target.slice(0, count)));
  }

  function reverse(la_) {
    // reset the metaI and numLines in this section
    metaI = 0;
    numLines = 0;

    var la = la_,
      lines = la.data,
      lnIdx = la.pos - 1,
      line = lines[lnIdx];

    while (true) {
      reverseCompPrint(line);
      lnIdx--;
      line = lines[lnIdx];
      if (line) {
        continue;
      }
      la = la.prev;
      if (la === undefined) {
        break;
      }
      lines = la.data;
      lnIdx = la.pos;
      lnIdx--;
      line = lines[lnIdx];
    }
    if ((metaI - numLines) % 60 !== 0) {
      output.push('\n');
    }
  }

  function read() {
    //let chunk = stdin.read(READ_SIZE);
    let chunk = getChunk();
    //isFinal = false;
    if (!chunk) {
      if (isFirst) {
        isFirst = false;
        return;
      }
      la.pos = lnIdx;
      reverse(la);
      return;
    } else if (chunk.length < READ_SIZE) {
      isFinal = true;
    }
    while (chunk) {
      while (true) {
        // if have read a partial header line, read the rest of it
        if (needHeader) {
          //    const headerEnds = chunk.indexOf('\n');
          const headerEnds = chunk.indexOf(10);
          output.push(headerPartial.toString('ascii') + decoder.decode(chunk.slice(0, headerEnds)));
          headerPartial = '';
          chunk = chunk.slice(headerEnds);
          needHeader = false;
        }
        //const caretIdx = chunk.indexOf('>');
        const caretIdx = chunk.indexOf(62);
        if (caretIdx > -1) {
          // there is a caret in this chunk -- process the first part of
          // the chunk and then continue the `while (true)` loop to process
          // the next part of the chunk
          lines[lnIdx] = chunk.slice(0, caretIdx);
          // set chunk to the next section
          chunk = chunk.slice(caretIdx);
          la.pos = lnIdx + 1;
          reverse(la);
          // reset the data holders
          la = headLA;
          lines = la.data;
          la.pos = 0;
          lnIdx = 0;
          // check to see if the entire header line is here
          //const headerEnds = chunk.indexOf('\n');
          const headerEnds = chunk.indexOf(10);
          if (headerEnds < 0) {
            needHeader = true;
            headerPartial = chunk;
            break;
          } else {
            // log the header
            //      console.log(chunk.slice(0, headerEnds).toString('ascii'));
            //        console.log(decoder.decode(chunk.slice(0, headerEnds)));
            output.push(decoder.decode(chunk.slice(0, headerEnds)));
            // continue processing the rest of the chunk
            chunk = chunk.slice(headerEnds);
          }
        } else {
          if (lnIdx === LA_LEN) {
            la.pos = LA_LEN;
            la = la.next || (la.next = new LinkedArray(la));
            lines = la.data;
            lines[0] = chunk;
            lnIdx = la.pos = 1;
          } else {
            lines[lnIdx] = chunk;
            lnIdx++;
          }
          break;
        }
      }

      counter++;
      chunk = checkIfNull(counter);
      //chunk = getChunk();
      if (chunk != null) {
        if (chunk.length < READ_SIZE) {
          isFinal = true;
        }
      }
    }
    if (isFinal) {
      la.pos = lnIdx;
      reverse(la);
    }
  }

  function checkIfNull(chunkCounter) {
    return chunkCounter == nullNumber
      ? ((nullNumber += nullLoop[indxNullLoop++ % 12]), null)
      : getChunk();
  }
}

export default revcomp;
