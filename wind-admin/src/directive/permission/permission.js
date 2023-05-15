import { store } from '@/stores'

export function permissionDirective(el, binding, vnode) {
  const { value } = binding
  const roles = store.permissionStore.permissions

  if (value && value instanceof Array && value.length > 0) {
    const permissionRoles = value

    const hasPermission = roles.some(role => {
      return permissionRoles.includes(role)
    })

    if (!hasPermission) {
      el.parentNode && el.parentNode.removeChild(el)
    }
  } else {
    throw new Error(`need roles! Like v-permission="['test:table:table:add','test:table:table:update']"`)
  }
}
