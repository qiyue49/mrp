import { defineStore } from 'pinia'
import defaultSettings from '@/settings'
import { ref } from 'vue'

export const settingStore = defineStore('setting', () => {
  const fullScreenFun = ref()
  const topMenu = ref(defaultSettings.topMenu)
  const title = ref(defaultSettings.title)
  const sidebarLogo = ref(defaultSettings.sidebarLogo)

  function changeSetting({ key, value }) {
    switch (key) {
      case 'title':
        title.value = value
        break
      case 'topMenu':
        topMenu.value = value
        break
      case 'sidebarLogo':
        sidebarLogo.value = value
        break
      default:
        break
    }
  }

  function setFun(fun) {
    fullScreenFun.value = fun
  }

  function toggleFullScreen() {
    fullScreenFun.value()
  }

  return { title, topMenu, sidebarLogo, changeSetting, setFun, toggleFullScreen }
})

