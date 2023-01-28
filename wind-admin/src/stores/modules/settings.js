import { defineStore } from 'pinia'
import { defaultSettings } from '@/settings'

export const settingStore = defineStore('setting', () => {
  const { showSettings, topMenu, tagsView, fixedHeader, sidebarLogo, fullScreen } = defaultSettings

  function changeSetting({ key, value }) {
    if (Object.prototype.hasOwnProperty.call(defaultSettings, key)) {
      defaultSettings[key] = value
    }
  }

  return { showSettings, topMenu, tagsView, fixedHeader, sidebarLogo, fullScreen, changeSetting }
})

