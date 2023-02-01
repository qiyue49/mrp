// const req = require.context('@/assets/icons/svg', false, /\.svg$/)
// const requireAll = requireContext => requireContext.keys()
//
// const re = /\.\/(.*)\.svg/
//
// const svgIcons = requireAll(req).map(i => {
//   return i.match(re)[1]
// })
const files = import.meta.globEager('@/assets/mdcImgs/*.png')
const svgIcons = Object.values(files).map((v) => v.default)

export default svgIcons
