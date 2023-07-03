import { defineStore } from 'pinia'
import { ref } from 'vue'

export const tagsViewStore = defineStore('tagsView', () => {
  const isReload = ref(true)
  const visitedViews = ref([])
  const cachedViews = ref([])

  function reload(view) {
    isReload.value = view
  }
  function addView(view) {
    addVisitedView(view)
    addCachedView(view)
  }
  function addVisitedView(view) {
    if (visitedViews.value.some(v => v.path === view.path)) return
    visitedViews.value.push(
      Object.assign({}, view, {
        title: view.meta.title || 'no-name'
      })
    )
  }
  function addCachedView(view) {
    if (cachedViews.value.includes(view.name)) {
      return
    }
    if (!view.meta.noCache) {
      cachedViews.value.push(view.name)
    }
  }

  function delView(view) {
    return new Promise(resolve => {
      delVisitedView(view)
      delCachedView(view)
      resolve({
        visitedViews: [...visitedViews.value],
        cachedViews: [...cachedViews.value]
      })
    })
  }
  function delVisitedView(view) {
    return new Promise(resolve => {
      for (let i = 0; i < visitedViews.value.length; i++) {
        const v = visitedViews.value[i]
        if (v.path === view.path) {
          visitedViews.value.splice(i, 1)
          break
        }
      }
      resolve([...visitedViews.value])
    })
  }
  function delCachedView(view) {
    return new Promise(resolve => {
      for (let i = 0; i < cachedViews.value.length; i++) {
        const v = cachedViews.value[i]
        if (v === view.name) {
          const index = cachedViews.value.indexOf(v)
          cachedViews.value.splice(index, 1)
          break
        }
      }
      resolve([...cachedViews.value])
    })
  }

  function delOthersViews(view) {
    return new Promise(resolve => {
      delOthersVisitedViews(view)
      delOthersCachedViews(view)
      resolve({
        visitedViews: [...visitedViews.value],
        cachedViews: [...cachedViews.value]
      })
    })
  }
  function delOthersVisitedViews(view) {
    return new Promise(resolve => {
      visitedViews.value = visitedViews.value.filter(v => {
        return v.meta.affix || v.path === view.path
      })
      resolve([...visitedViews.value])
    })
  }
  function delOthersCachedViews(view) {
    return new Promise(resolve => {
      for (let i = 0; i < cachedViews.value.length; i++) {
        const v = cachedViews.value[i]
        if (v === view.name) {
          const index = cachedViews.value.indexOf(v)
          cachedViews.value = cachedViews.value.slice(index, index + 1)
          break
        }
      }
      resolve([...cachedViews.value])
    })
  }

  function delAllViews(view) {
    return new Promise(resolve => {
      delAllVisitedViews(view)
      delAllCachedViews(view)
      resolve({
        visitedViews: [...visitedViews.value],
        cachedViews: [...cachedViews.value]
      })
    })
  }
  function delAllVisitedViews() {
    return new Promise(resolve => {
      // keep affix tags
      const affixTags = visitedViews.value.filter(tag => tag.meta.affix)
      visitedViews.value = affixTags
      resolve([...visitedViews.value])
    })
  }
  function delAllCachedViews() {
    return new Promise(resolve => {
      cachedViews.value = []
      resolve([...cachedViews.value])
    })
  }

  function updateVisitedView(view) {
    for (let i = 0; i < visitedViews.value.length; i++) {
      let v = visitedViews.value[i]
      if (v.path === view.path) {
        v = Object.assign(v, view)
        break
      }
    }
  }
  return { isReload, visitedViews, cachedViews, reload, addVisitedView, addView, delView, delAllViews, delCachedView, updateVisitedView, delOthersViews }
})

