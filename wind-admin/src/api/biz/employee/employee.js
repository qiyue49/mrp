import request from '@/utils/request'

export function fetchEmployeeList(data) {
  return request({
    url: '/biz/employee/employee/list',
    method: 'post',
    data
  })
}

export function getEmployee(id) {
  return request({
    url: '/biz/employee/employee/detail/' + id,
    method: 'get',
    params: { id }
  })
}

export function createEmployee(data) {
  return request({
    url: '/biz/employee/employee/add',
    method: 'post',
    data
  })
}

export function updateEmployee(data) {
  return request({
    url: '/biz/employee/employee/update',
    method: 'post',
    data
  })
}

export function deleteEmployee(ids) {
  return request({
    url: '/biz/employee/employee/delete',
    method: 'post',
    data: { ids }
  })
}

