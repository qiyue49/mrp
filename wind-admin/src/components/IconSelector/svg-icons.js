function getFileName(url) {
  let name = ''
  if (url !== null && url !== '') {
    name = url.substring(url.lastIndexOf('/') + 1)
  } else {
    name = ''
  }
  return name.split('.')[0]
}
const files = import.meta.globEager('@/assets/icons/svg/*.svg')
console.log('Object.values(files)', Object.values(files))
const svgIcons = Object.values(files).map(i => {
  return getFileName(i.default)
})

export default svgIcons
