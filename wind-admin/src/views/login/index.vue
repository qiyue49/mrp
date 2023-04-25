<template>
  <login1 v-if="index === 1" />
  <login2 v-else-if="index === 2" />
  <login3 v-else-if="index === 3" />
  <login4 v-else-if="index === 4" />
  <login5 v-else-if="index === 5" />
</template>

<script>
import Login1 from './login1'
import Login2 from './login2'
import Login3 from './login3'
import Login4 from './login4'
import Login5 from './login5'

export default {
  name: 'Login',
  components: { Login1, Login2, Login3, Login4, Login5 },
  data() {
    return {
      index: 2
    }
  },
  created() {
    // this.index = 5
    this.index = this.getRandomArbitrary(1, 5)
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
