<template>
  <login0 v-if="index === 0" />
  <login1 v-else-if="index === 1" />
  <login3 v-else-if="index === 2" />
  <login4 v-else-if="index === 3" />
</template>

<script>
import Login0 from './login0'
import Login1 from './login1'
import Login3 from './login3'
import Login4 from './login4'
export default {
  name: 'Login',
  components: { Login1, Login0, Login3, Login4 },
  data() {
    return {
      index: 0
    }
  },
  created() {
    this.index = this.getRandomArbitrary(0, 4)
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
