<template>
  <div class="not-found">
    <h1>404 Not Found</h1>
    <p>The page you are looking for does not exist.</p>
    <el-button type="primary" @click="$router.push('/dashboard')">Back to Dashboard</el-button>
  </div>
</template>

<style scoped>
.not-found {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
  padding: 40px;
}
</style>
