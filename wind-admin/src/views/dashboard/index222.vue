<template>
  <!-- <swiper
    :modules="modules"
    :slides-per-view="5"
    :space-between="50"
    navigation
    @swiper="onSwiper"
    @slideChange="onSlideChange"
  >
    <swiper-slide v-for="item in data" :key="item">
      <div class="btn">
        <span>{{ item.con }}</span>
        <span>{{ item.tit }}</span>
      </div>
    </swiper-slide>
  </swiper> -->
  <div class="container">
    <swiper
      :modules="modules"
      :slides-per-view="5"
      :space-between="50"
      navigation>
      <swiper-slide>Slide 1</swiper-slide>
      <swiper-slide>Slide 2</swiper-slide>
      <swiper-slide>Slide 3</swiper-slide>
      <swiper-slide>Slide 4</swiper-slide>
      <swiper-slide>Slide 5</swiper-slide>
      <swiper-slide>Slide 6</swiper-slide>
      <swiper-slide>Slide 6</swiper-slide>
      <swiper-slide>Slide 6</swiper-slide>
      <swiper-slide>Slide 6</swiper-slide>
      <swiper-slide>Slide 6</swiper-slide>
    </swiper>
  </div>
</template>
<script>
// import Swiper core and required modules
import { Navigation, Pagination, Scrollbar, A11y } from 'swiper'

// Import Swiper Vue.js components
import { Swiper, SwiperSlide } from 'swiper/vue'
import { reactive,ref } from "vue";
import { Navigation } from 'swiper';
import { Swiper, SwiperSlide } from 'swiper/vue';
import 'swiper/css';
import 'swiper/css/navigation';
const modules = [Navigation]
// Import Swiper styles
import 'swiper/css'
import 'swiper/css/navigation'
import 'swiper/css/pagination'
import 'swiper/css/scrollbar'

// Import Swiper styles
export default {
  components: {
    Swiper,
    SwiperSlide
  },
  setup() {
    const onSwiper = (swiper) => {
      console.log(swiper)
    }
    const onSlideChange = () => {
      console.log('slide change')
    }
    return {
      onSwiper,
      onSlideChange,
      modules: [Navigation, Pagination, Scrollbar, A11y]
    }
  },
  data() {
    return {
      data: [
        { con: 16, tit: '周四' },
        { con: 16, tit: '周四' },
        { con: 16, tit: '周四' },
        { con: 16, tit: '周四' },
        { con: 16, tit: '周四' },
        { con: 16, tit: '周四' },
        { con: 16, tit: '周四' },
        { con: 16, tit: '周四' },
        { con: 16, tit: '周四' },
        { con: 16, tit: '周四' },
        { con: 16, tit: '周四' }
      ]
    }
  }
}
</script>

<style lang="scss" scoped>
.container{
    position: relative;
  }
  :deep(.swiper) {
    position: initial;
    .swiper-wrapper{
      transition: all 1s !important;
    }
    .swiper-slide {
      width: 240px;
      height: 305px;
      background-color: #27bb9a;
    }
    .swiper-button-prev {
      left: 1190px;
    }
    .swiper-button-next {
      left: 1220px;
    }
    .swiper-button-prev,
    .swiper-button-next {
      width: 20px;
      height: 20px;
      background-color: #27ba9b;
      top: 80px;
      &::after {
        font-size: 12px;
        color: white;
      }
    }
    .swiper-button-disabled{
      background-color:#ccc;
      cursor:not-allowed;
      pointer-events:initial;
    }
  }
// .swiper{
//   width: 75%;
//   .swiper-wrapper{
//     width: 70% !important;
//   }
//   .swiper-slide{
//     margin: 10px !important;
//     // padding: 10px 20px;
//     width: 15% !important;
//   }
// }
.btn{
  background-color: #F3F5F8;
  display: inline-block;
  padding: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.all{
  width: 100%;
  overflow: hidden;
  margin: 20px 0;
  display: flex;
  div{
    padding: 20px;
    border: 1px solid ;
  }
}
</style>