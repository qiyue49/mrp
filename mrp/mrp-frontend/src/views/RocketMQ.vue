<template>
  <div class="rocketmq-container">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <el-icon class="title-icon"><Message /></el-icon>
          RocketMQ Management
        </h1>
        <p class="page-subtitle">Monitor message queues, manage topics, and track message delivery</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="sendTestMessage">
          <el-icon><ChatLineRound /></el-icon> Send Test Message
        </el-button>
      </div>
    </div>

    <!-- Status Card -->
    <div class="status-section">
      <el-card class="status-card">
        <div class="status-grid">
          <div class="status-item">
            <div class="status-label">Broker Status</div>
            <div class="status-value">
              <el-tag :type="brokerStatus === 'Online' ? 'success' : 'danger'">
                {{ brokerStatus }}
              </el-tag>
            </div>
          </div>
          <div class="status-item">
            <div class="status-label">Topics</div>
            <div class="status-value">{{ topics.length }}</div>
          </div>
          <div class="status-item">
            <div class="status-label">Consumers</div>
            <div class="status-value">{{ consumers.length }}</div>
          </div>
          <div class="status-item">
            <div class="status-label">Messages</div>
            <div class="status-value">{{ messageCount }}</div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- Topics Table -->
    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span><el-icon><Collection /></el-icon> Topics</span>
          <el-button type="primary" size="small" @click="createTopic">
            <el-icon><Plus /></el-icon> Create Topic
          </el-button>
        </div>
      </template>
      <el-table :data="topics" style="width: 100%" border stripe>
        <el-table-column prop="name" label="Topic Name" width="200" />
        <el-table-column prop="status" label="Status" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'Active' ? 'success' : 'danger'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="messageCount" label="Messages" width="100" />
        <el-table-column prop="createdAt" label="Created At" width="180" />
        <el-table-column label="Actions" width="120" fixed="right">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              @click="viewTopic(scope.row)"
              class="action-btn"
            >
              <el-icon><View /></el-icon>
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="deleteTopic(scope.row.name)"
              class="action-btn"
            >
              <el-icon><Delete /></el-icon>
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Messages Table -->
    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span><el-icon><Message /></el-icon> Recent Messages</span>
        </div>
      </template>
      <el-table :data="messages" style="width: 100%" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="topic" label="Topic" width="150" />
        <el-table-column prop="content" label="Content" />
        <el-table-column prop="status" label="Status" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'Delivered' ? 'success' : 'warning'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sentAt" label="Sent At" width="180" />
      </el-table>
    </el-card>

    <!-- Create Topic Dialog -->
    <el-dialog
      v-model="createTopicVisible"
      title="Create New Topic"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="topicForm" :rules="topicRules" ref="topicFormRef" label-width="120px">
        <el-form-item label="Topic Name" prop="name">
          <el-input v-model="topicForm.name" placeholder="Enter topic name" />
        </el-form-item>
        <el-form-item label="Description" prop="description">
          <el-input v-model="topicForm.description" type="textarea" placeholder="Enter topic description" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="createTopicVisible = false">Cancel</el-button>
          <el-button type="primary" @click="handleCreateTopic">Create</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- Test Message Dialog -->
    <el-dialog
      v-model="testMessageVisible"
      title="Send Test Message"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="testMessageForm" :rules="testMessageRules" ref="testMessageFormRef" label-width="120px">
        <el-form-item label="Topic" prop="topic">
          <el-select v-model="testMessageForm.topic" placeholder="Select topic">
            <el-option v-for="topic in topics" :key="topic.name" :label="topic.name" :value="topic.name" />
          </el-select>
        </el-form-item>
        <el-form-item label="Content" prop="content">
          <el-input v-model="testMessageForm.content" type="textarea" placeholder="Enter message content" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="testMessageVisible = false">Cancel</el-button>
          <el-button type="primary" @click="handleSendTestMessage">Send</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Message, ChatLineRound, Collection, View, Delete, Plus } from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'

const brokerStatus = ref('Online')
const messageCount = ref(125)

const topics = ref([
  { name: 'orders-events', status: 'Active', messageCount: 45, createdAt: '2024-01-01 10:00:00' },
  { name: 'inventory-updates', status: 'Active', messageCount: 32, createdAt: '2024-01-02 11:00:00' },
  { name: 'production-status', status: 'Active', messageCount: 28, createdAt: '2024-01-03 12:00:00' },
  { name: 'supplier-notifications', status: 'Inactive', messageCount: 20, createdAt: '2024-01-04 13:00:00' }
])

const consumers = ref([
  { name: 'order-consumer', status: 'Online', topic: 'orders-events' },
  { name: 'inventory-consumer', status: 'Online', topic: 'inventory-updates' },
  { name: 'production-consumer', status: 'Online', topic: 'production-status' }
])

const messages = ref([
  { id: 1, topic: 'orders-events', content: 'Order created: SO0001', status: 'Delivered', sentAt: '2024-01-15 10:00:00' },
  { id: 2, topic: 'inventory-updates', content: 'Stock updated: MAT001', status: 'Delivered', sentAt: '2024-01-15 10:05:00' },
  { id: 3, topic: 'production-status', content: 'WO0001 completed', status: 'Delivered', sentAt: '2024-01-15 10:10:00' },
  { id: 4, topic: 'orders-events', content: 'Order updated: SO0002', status: 'Pending', sentAt: '2024-01-15 10:15:00' }
])

const createTopicVisible = ref(false)
const testMessageVisible = ref(false)
const topicFormRef = ref(null)
const testMessageFormRef = ref(null)

const topicForm = ref({
  name: '',
  description: ''
})

const topicRules = {
  name: [{ required: true, message: 'Please enter topic name', trigger: 'blur' }],
  description: [{ required: true, message: 'Please enter description', trigger: 'blur' }]
}

const testMessageForm = ref({
  topic: '',
  content: ''
})

const testMessageRules = {
  topic: [{ required: true, message: 'Please select topic', trigger: 'blur' }],
  content: [{ required: true, message: 'Please enter message content', trigger: 'blur' }]
}

const createTopic = () => {
  topicForm.value = {
    name: '',
    description: ''
  }
  createTopicVisible.value = true
}

const handleCreateTopic = async () => {
  if (!topicFormRef.value) return
  
  try {
    await topicFormRef.value.validate()
    
    // Add new topic
    topics.value.push({
      name: topicForm.value.name,
      status: 'Active',
      messageCount: 0,
      createdAt: new Date().toLocaleString()
    })
    
    createTopicVisible.value = false
    ElMessage.success('Topic created successfully')
  } catch (error) {
    console.error('Validation error:', error)
  }
}

const viewTopic = (topic) => {
  console.log('View topic:', topic)
  // Implement view topic functionality
}

const deleteTopic = (topicName) => {
  ElMessageBox.confirm(
    `Are you sure you want to delete topic "${topicName}"?`,
    'Warning',
    {
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
      type: 'warning'
    }
  ).then(() => {
    // Remove topic
    topics.value = topics.value.filter(topic => topic.name !== topicName)
    ElMessage.success('Topic deleted successfully')
  }).catch(() => {
    // Cancel operation
  })
}

const sendTestMessage = () => {
  testMessageForm.value = {
    topic: topics.value[0]?.name || '',
    content: 'Test message from MRP system'
  }
  testMessageVisible.value = true
}

const handleSendTestMessage = async () => {
  if (!testMessageFormRef.value) return
  
  try {
    await testMessageFormRef.value.validate()
    
    // Add new message
    messages.value.unshift({
      id: messages.value.length + 1,
      topic: testMessageForm.value.topic,
      content: testMessageForm.value.content,
      status: 'Delivered',
      sentAt: new Date().toLocaleString()
    })
    
    // Update message count
    messageCount.value++
    
    // Update topic message count
    const topic = topics.value.find(t => t.name === testMessageForm.value.topic)
    if (topic) {
      topic.messageCount++
    }
    
    testMessageVisible.value = false
    ElMessage.success('Test message sent successfully')
  } catch (error) {
    console.error('Validation error:', error)
  }
}

onMounted(() => {
  // Load initial data if needed
})
</script>

<style scoped>
.rocketmq-container {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 4px;
}

.header-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
  color: #111827;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 12px;
}

.title-icon {
  color: #1a56db;
}

.page-subtitle {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
}

.status-section {
  margin-bottom: 16px;
}

.status-card {
  border-radius: 12px;
}

.status-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 24px;
}

.status-item {
  text-align: center;
  padding: 16px;
  border-radius: 8px;
  background: #f9fafb;
}

.status-label {
  font-size: 13px;
  color: #6b7280;
  margin-bottom: 8px;
}

.status-value {
  font-size: 20px;
  font-weight: 700;
  color: #111827;
}

.table-card {
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.action-btn {
  margin-right: 8px;
}

.dialog-footer {
  width: 100%;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
