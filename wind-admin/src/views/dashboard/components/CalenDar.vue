<template>
  <div class="container">
    <swiper
      :modules="modules"
      :slides-per-view="5"
      :space-between="50"
      navigation
      @swiper="onSwiper"
      @slide-change="onSlideChange"
    >
      <swiper-slide v-for="item in data" :key="item.id">
        <div :class="click==item.id?'selectbtn':'btn'" @click="select(item)">
          <span class="text">{{ item.con }}</span>
          <span class="week">{{ item.tit }}</span>
        </div>
      </swiper-slide>
    </swiper>
  </div>
</template>
<script>
// import Swiper core and required modules
import { Navigation, Pagination, Scrollbar, A11y } from 'swiper'

// Import Swiper Vue.js components
import { Swiper, SwiperSlide } from 'swiper/vue'

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
    }
    const onSlideChange = () => {
    }
    return {
      onSwiper,
      onSlideChange,
      modules: [Navigation, Pagination, Scrollbar, A11y]
    }
  },
  data() {
    return {
      click: 1,
      data: [
        { id: 1, con: 16, tit: '周四' },
        { id: 2, con: 16, tit: '周四' },
        { id: 3, con: 16, tit: '周四' },
        { id: 4, con: 16, tit: '周四' },
        { id: 5, con: 16, tit: '周四' },
        { id: 6, con: 16, tit: '周四' },
        { id: 7, con: 16, tit: '周四' },
        { id: 8, con: 16, tit: '周四' }
      ]
    }
  },
  methods: {
    select(item) {
      this.click = item.id
    }
  }
}
</script>

<style lang="scss" scoped>
.container{
    position: relative;
    // margin: 20px 0;
    width: 85% !important;
  }
  :deep(.swiper) {
    position: initial;
    .swiper-wrapper{
      transition: all 1s !important;
    }
    .swiper-slide {
      margin: 10px !important;
//     // padding: 10px 20px;
    width: 15% !important;
      // height: 50px;
      // background-color: #27bb9a;
    }
    .swiper-button-prev {
      left: -35px;
    }
    .swiper-button-next {
      right: -35px;
    }
    .swiper-button-prev,
    .swiper-button-next {
      width: 20px;
      height: 20px;
      // background-color: #27ba9b;
      // color: #1762F2 ;
      top: 55px;
      &::after {
        font-size: 15px;
        color: #1762F2;
        font-weight: 900;
      }
    }
    .swiper-button-disabled{
      background-color:#ccc;
      cursor:not-allowed;
      pointer-events:initial;
    }

    .text{
      font-size: 20px;
      font-family: DINAlternate-Bold, DINAlternate;
      font-weight: bold;
    }

    .week{
      font-size: 14px;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      letter-spacing: 2px;
    }
  }
.btn{
  background-color: var(--ep-color-background);
  display: inline-block;
  padding: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-radius: 5px;
}
.selectbtn{
  background-color: #1762F2;
  color: #fff;
  display: inline-block;
  padding: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-radius: 5px;
}
// .all{
//   width: 100%;
//   overflow: hidden;
//   margin: 20px 0;
//   display: flex;
//   div{
//     padding: 20px;
//     border: 1px solid ;
//   }
// }
</style>
