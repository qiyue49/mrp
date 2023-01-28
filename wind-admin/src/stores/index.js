import { createPinia } from 'pinia'
import { dictStore } from './modules/dict'
import { userStore } from './modules/user'
import { sysConfigStore } from './modules/sysConfig'
import { settingStore } from './modules/settings'
import { appStore } from './modules/app'
import { permissionStore } from './modules/permission'
import { tagsViewStore } from './modules/tagsView'

const pinia = createPinia()
pinia.init = function () {
  pinia.userStore = userStore(pinia)
  pinia.dictStore = dictStore(pinia)
  pinia.sysConfigStore = sysConfigStore(pinia)
  pinia.settingStore = settingStore(pinia)
  pinia.appStore = appStore(pinia)
  pinia.permissionStore = permissionStore(pinia)
  pinia.tagsViewStore = tagsViewStore(pinia)
}
export default pinia
