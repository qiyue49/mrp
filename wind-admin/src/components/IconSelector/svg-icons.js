function getFileName(url) {
  let name = ''
  let dir = ''
  if (url !== null && url !== '') {
    name = url.replace('/src/assets/icons/svg/', '')
    const fileName = name.split('/')
    dir = fileName[0]
    name = fileName[1]
  } else {
    name = ''
  }
  return dir + '-' + name.split('.')[0]
}
const files = import.meta.glob('@/assets/icons/svg/**/*.svg', { eager: true })
// console.log('Object.values(files)', Object.values(files))
const svgIcons = Object.values(files).map(i => {
  return getFileName(i.default)
})
// console.log('svgIcons', svgIcons)
export default svgIcons
