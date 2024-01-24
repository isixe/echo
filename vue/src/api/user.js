import request from '@/utils/request'

export async function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}

export function register() {
  return request({
    url: '/register',
    method: 'post'
  })
}
