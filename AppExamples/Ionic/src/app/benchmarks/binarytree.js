const output = [];

async function binarytree(param, writer){
  mainThread(param, writer);
  return writer(output.join( "\n"));
};

function mainThread (param, writer) {
  const maxDepth = Math.max(6, param);

  const stretchDepth = maxDepth + 1;
  const check = itemCheck(bottomUpTree(stretchDepth));
  output.push(`stretch tree of depth ${stretchDepth}\t check: ${check}`);

  const longLivedTree = bottomUpTree(maxDepth);

  for (let depth = 4; depth <= maxDepth; depth += 2) {
    const iterations = 1 << (maxDepth - depth + 4);
    work(iterations, depth, writer);
  }
  output.push(`long lived tree of depth ${maxDepth}\t check: ${itemCheck(longLivedTree)}`);
};

function work (iterations, depth, writer) {
  let check = 0;
  for (let i = 0; i < iterations; i++) {
    check += itemCheck(bottomUpTree(depth));
  }
  output.push(`${iterations}\t trees of depth ${depth}\t check: ${check}`);
};

function TreeNode (left, right) {
  return {left, right};
};

const itemCheck = node => {
  if (node.left === null) {
    return 1;
  }
  return 1 + itemCheck(node.left) + itemCheck(node.right);
};

const bottomUpTree = depth => {
  return depth > 0
    ? new TreeNode(bottomUpTree(depth - 1), bottomUpTree(depth - 1))
    : new TreeNode(null, null);
};

export default binarytree;
