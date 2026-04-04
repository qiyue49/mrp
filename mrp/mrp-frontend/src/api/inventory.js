import api from './index'

export const InventoryService = {
  // Create inventory
  createInventory: (inventory) => {
    return api.post('/inventory', inventory)
  },
  
  // Get all inventory
  getAllInventory: () => {
    return api.get('/inventory')
  },
  
  // Get inventory by material code
  getInventoryByMaterialCode: (materialCode) => {
    return api.get(`/inventory/material/${materialCode}`)
  },
  
  // Update inventory
  updateInventory: (id, inventory) => {
    return api.put(`/inventory/${id}`, inventory)
  },
  
  // Delete inventory
  deleteInventory: (id) => {
    return api.delete(`/inventory/${id}`)
  }
}
