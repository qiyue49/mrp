import Cookies from 'js-cookie'
import { defineStore } from 'pinia'

export const appStore = defineStore('app', () => {
  const sidebar = {
    opened: Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : true,
    withoutAnimation: false
  }
  const device = 'desktop'

  const size = Cookies.get('size') || 'medium'

  function toggleSideBar() {
    sidebar.opened = !sidebar.opened
    sidebar.withoutAnimation = false
    if (sidebar.opened) {
      Cookies.set('sidebarStatus', 1)
    } else {
      Cookies.set('sidebarStatus', 0)
    }
  }
  function closeSideBar(withoutAnimation) {
    Cookies.set('sidebarStatus', 0)
    sidebar.opened = false
    sidebar.withoutAnimation = withoutAnimation
  }
  function toggleDevice(device) {
    this.device = device
  }

  function setSize(size) {
    this.size = size
    Cookies.set('size', size)
  }
  return { sidebar, device, size, toggleDevice, closeSideBar, toggleSideBar, setSize }
})
