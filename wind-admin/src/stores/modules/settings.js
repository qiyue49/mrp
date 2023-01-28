import { defineStore } from 'pinia'
import defaultSettings from '@/settings'

export const settingStore = defineStore('setting', () => {
  const { title, showSettings, topMenu, tagsView, fixedHeader, sidebarLogo, fullScreen } = defaultSettings

  function changeSetting({ key, value }) {
    if (Object.prototype.hasOwnProperty.call(defaultSettings, key)) {
      defaultSettings[key] = value
    }
  }

  return { title, showSettings, topMenu, tagsView, fixedHeader, sidebarLogo, fullScreen, changeSetting }
})

