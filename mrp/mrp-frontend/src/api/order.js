import api from './index'

export const OrderService = {
  // Create order
  createOrder: (order) => {
    return api.post('/orders', order)
  },
  
  // Get all orders
  getAllOrders: () => {
    return api.get('/orders')
  },
  
  // Get order by ID
  getOrderById: (id) => {
    return api.get(`/orders/${id}`)
  },
  
  // Get order by code
  getOrderByCode: (code) => {
    return api.get(`/orders/code/${code}`)
  },
  
  // Update order status
  updateOrderStatus: (id, status, updatedBy) => {
    return api.put(`/orders/${id}/status`, null, {
      params: { status, updatedBy }
    })
  }
}
