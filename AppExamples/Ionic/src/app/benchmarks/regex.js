async function regex(fileText, writer) {
  const output = []
  const regExps = [
    /agggtaaa|tttaccct/gi,
    /[cgt]gggtaaa|tttaccc[acg]/gi,
    /a[act]ggtaaa|tttacc[agt]t/gi,
    /ag[act]gtaaa|tttac[agt]ct/gi,
    /agg[act]taaa|ttta[agt]cct/gi,
    /aggg[acg]aaa|ttt[cgt]ccct/gi,
    /agggt[cgt]aa|tt[acg]accct/gi,
    /agggta[cgt]a|t[acg]taccct/gi,
    /agggtaa[cgt]|[acg]ttaccct/gi,
  ];

  const initialLen = fileText.length;
  fileText = fileText.replace(/^>.*\n|\n/gm, '');
  const cleanedLen = fileText.length;

  for (let j = 0; j < regExps.length; j++) {
    const re = regExps[j];
    const m = fileText.match(re);
    output.push(re.source + ' ' + (m ? m.length : 0));
  }

  const endLen = fileText
    .replace(/tHa[Nt]/g, '<4>')
    .replace(/aND|caN|Ha[DS]|WaS/g, '<3>')
    .replace(/a[NSt]|BY/g, '<2>')
    .replace(/<[^>]*>/g, '|')
    .replace(/\|[^|][^|]*\|/g, '-').length;
   
    output.push(initialLen, cleanedLen, endLen);
    await writer(output.join("\n"))
}

export default regex;
