import request from '@/utils/request'

export function fetchMenuList(query) {
  return request({
    url: '/sys/menu/list',
    method: 'get',
    params: query
  })
}

export function fetchMenu(id) {
  return request({
    url: '/sys/menu/detail/' + id,
    method: 'get'
  })
}

export function getMenus() {
  return request({
    url: '/sys/menu/getMenus',
    method: 'get'
  })
}

export function createMenu(data) {
  return request({
    url: '/sys/menu/add',
    method: 'post',
    data
  })
}

export function changeSort(rowId, value) {
  return request({
    url: '/sys/menu/' + rowId + '/changeSort',
    method: 'post',
    data: { sort: value }
  })
}

export function updateMenu(data) {
  return request({
    url: '/sys/menu/update',
    method: 'post',
    data
  })
}

export function deleteMenu(ids) {
  return request({
    url: '/sys/menu/delete',
    method: 'post',
    data: { ids }
  })
}

export function generateButton(data) {
  return request({
    url: '/sys/menu/' + data.menuId + '/generate/button',
    method: 'post',
    data
  })
}

export function fetchPermissionList() {
  return request({
    url: '/sys/menu/getPermissions',
    method: 'get'
  })
}

