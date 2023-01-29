<template>
  <div class="dashboard-container">
    11111111
    <component :is="currentRole" />
  </div>
</template>

<script>
import { mapState } from 'pinia'
import adminDashboard from './admin'
import { userStore } from '@/stores/modules/user'

export default {
  name: 'Dashboard',
  components: { adminDashboard },
  data() {
    return {
      currentRole: 'adminDashboard'
    }
  },
  computed: {
    ...mapState(userStore, [
      'roles'
    ])
  },
  created() {

  }
}
</script>
