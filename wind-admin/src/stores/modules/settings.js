import { defineStore } from 'pinia'
import defaultSettings from '@/settings'
import { reactive, ref } from 'vue'

export const settingStore = defineStore('setting', () => {
  const { title, showSettings, topMenu, tagsView, fixedHeader, sidebarLogo } = reactive(defaultSettings)

  const fullScreenFun = ref()

  function changeSetting({ key, value }) {
    console.log(3333)
    if (Object.prototype.hasOwnProperty.call(defaultSettings, key)) {
      defaultSettings[key] = value
    }
  }

  function setFun(fun) {
    fullScreenFun.value = fun
  }

  function toggleFullScreen() {
    fullScreenFun.value()
  }

  return { title, showSettings, topMenu, tagsView, fixedHeader, sidebarLogo, changeSetting, setFun, toggleFullScreen }
})

