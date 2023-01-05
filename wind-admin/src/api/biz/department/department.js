import request from '@/utils/request'

export function fetchDepartmentList(data) {
  return request({
    url: '/biz/department/department/list',
    method: 'post',
    data
  })
}

export function getDepartment(id) {
  return request({
    url: '/biz/department/department/detail/' + id,
    method: 'get',
    params: { id }
  })
}

export function createDepartment(data) {
  return request({
    url: '/biz/department/department/add',
    method: 'post',
    data
  })
}

export function updateDepartment(data) {
  return request({
    url: '/biz/department/department/update',
    method: 'post',
    data
  })
}

export function deleteDepartment(ids) {
  return request({
    url: '/biz/department/department/delete',
    method: 'post',
    data: { ids }
  })
}

