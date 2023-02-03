import { createPinia } from 'pinia'
import { dictStore } from './modules/dict'
import { userStore } from './modules/user'
import { sysConfigStore } from './modules/sysConfig'
import { settingStore } from './modules/settings'
import { appStore } from './modules/app'
import { permissionStore } from './modules/permission'
import { tagsViewStore } from './modules/tagsView'

const pinia = createPinia()
export const store = {
  userStore: userStore(pinia),
  dictStore: dictStore(pinia),
  sysConfigStore: sysConfigStore(pinia),
  settingStore: settingStore(pinia),
  appStore: appStore(pinia),
  permissionStore: permissionStore(pinia),
  tagsViewStore: tagsViewStore(pinia)
}
export default pinia
