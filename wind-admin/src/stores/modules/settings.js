import { defineStore } from 'pinia'
import defaultSettings from '@/settings'
import { reactive } from 'vue'

export const settingStore = defineStore('setting', () => {
  const { title, showSettings, topMenu, tagsView, fixedHeader, sidebarLogo, fullScreen } = reactive(defaultSettings)

  function changeSetting({ key, value }) {
    if (Object.prototype.hasOwnProperty.call(defaultSettings, key)) {
      defaultSettings[key] = value
    }
  }

  return { title, showSettings, topMenu, tagsView, fixedHeader, sidebarLogo, fullScreen, changeSetting }
})

