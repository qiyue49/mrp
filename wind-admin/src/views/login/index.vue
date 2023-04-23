<template>
  <login1 v-if="index === 1" />
  <login3 v-else-if="index === 2" />
  <login4 v-else-if="index === 3" />
  <login5 v-else-if="index === 4" />
</template>

<script>
import Login1 from './login1'
import Login3 from './login3'
import Login4 from './login4'
import Login5 from './login5'

export default {
  name: 'Login',
  components: { Login1, Login3, Login4, Login5 },
  data() {
    return {
      index: 1
    }
  },
  created() {
    this.index = 1
    // console.log('index', this.index)
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
