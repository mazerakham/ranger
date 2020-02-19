
export const bRange = (b) => !b || b === "0" ? [] : [...bRange(b-1), b-1];

export const abRange = (a, b) => b <= a ? [] : [...abRange(a, b-1), b-1];

export const cartesian = (list1, list2) => {
  let ret = [];
  for (let i = 0; i < list1.length; i++) {
    for (let j = 0; j < list2.length; j++) {
      ret.push([list1[i], list2[j]]);
    }
  }
  return ret;
}

export const enumerate = (list) => {
  let ret = [];
  for (let i = 0; i < list.length; i++) {
    ret.push([i, list[i]]);
  }
  return ret;
}

export const clip = (min, max, val) => {
  return Math.max(min, Math.min(max, val));
}