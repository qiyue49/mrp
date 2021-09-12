<template>
  <login0 v-if="index === 0" />
  <login1 v-else-if="index === 1" />
</template>

<script>
import Login0 from './login0'
import Login1 from './login1'
export default {
  name: 'Login',
  components: { Login1, Login0 },
  data() {
    return {
      index: 0
    }
  },
  created() {
    this.index = this.getRandomArbitrary(0, 2)
    console.log('index', this.index)
  },
  methods: {
    getRandomArbitrary(min, max) {
      return Math.floor(Math.random() * (max - min) + min)
    }
  }
}
</script>

<style scoped>

</style>
