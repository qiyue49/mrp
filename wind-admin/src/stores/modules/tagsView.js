import { defineStore } from 'pinia'

export const tagsViewStore = defineStore('tagsView', () => {
  let isReload = true
  let visitedViews = []
  let cachedViews = []

  function reload(view) {
    isReload = view
  }
  function addView(view) {
    addVisitedView(view)
    addCachedView(view)
  }
  function addVisitedView(view) {
    if (visitedViews.some(v => v.path === view.path)) return
    visitedViews.push(
      Object.assign({}, view, {
        title: view.meta.title || 'no-name'
      })
    )
  }
  function addCachedView(view) {
    if (cachedViews.includes(view.name)) {
      return
    }
    if (!view.meta.noCache) {
      cachedViews.push(view.name)
    }
  }

  function delView(view) {
    return new Promise(resolve => {
      delVisitedView(view)
      delCachedView(view)
      resolve({
        visitedViews: [...visitedViews],
        cachedViews: [...cachedViews]
      })
    })
  }
  function delVisitedView(view) {
    return new Promise(resolve => {
      for (let i = 0; i < visitedViews.length; i++) {
        const v = visitedViews[i]
        if (v.path === view.path) {
          visitedViews.splice(i, 1)
          break
        }
      }
      resolve([...visitedViews])
    })
  }
  function delCachedView(view) {
    return new Promise(resolve => {
      for (let i = 0; i < cachedViews.length; i++) {
        const v = cachedViews[i]
        if (v === view.name) {
          const index = cachedViews.indexOf(v)
          cachedViews.splice(index, 1)
          break
        }
      }
      resolve([...cachedViews])
    })
  }

  function delOthersViews(view) {
    return new Promise(resolve => {
      delOthersVisitedViews(view)
      delOthersCachedViews(view)
      resolve({
        visitedViews: [...visitedViews],
        cachedViews: [...cachedViews]
      })
    })
  }
  function delOthersVisitedViews(view) {
    return new Promise(resolve => {
      visitedViews = visitedViews.filter(v => {
        return v.meta.affix || v.path === view.path
      })
      resolve([...visitedViews])
    })
  }
  function delOthersCachedViews(view) {
    return new Promise(resolve => {
      for (let i = 0; i < cachedViews.length; i++) {
        const v = cachedViews[i]
        if (v === view.name) {
          const index = cachedViews.indexOf(v)
          cachedViews = cachedViews.slice(index, index + 1)
          break
        }
      }
      resolve([...cachedViews])
    })
  }

  function delAllViews(view) {
    return new Promise(resolve => {
      delAllVisitedViews(view)
      delAllCachedViews(view)
      resolve({
        visitedViews: [...visitedViews],
        cachedViews: [...cachedViews]
      })
    })
  }
  function delAllVisitedViews({ commit, state }) {
    return new Promise(resolve => {
      // keep affix tags
      const affixTags = visitedViews.filter(tag => tag.meta.affix)
      visitedViews = affixTags
      resolve([...visitedViews])
    })
  }
  function delAllCachedViews({ commit, state }) {
    return new Promise(resolve => {
      cachedViews = []
      resolve([...cachedViews])
    })
  }

  function updateVisitedView(view) {
    for (let i = 0; i < visitedViews.length; i++) {
      let v = visitedViews[i]
      if (v.path === view.path) {
        v = Object.assign(v, view)
        break
      }
    }
  }
  return { isReload, visitedViews, cachedViews, reload, addView, delView, delAllViews, updateVisitedView, delOthersViews }
})

