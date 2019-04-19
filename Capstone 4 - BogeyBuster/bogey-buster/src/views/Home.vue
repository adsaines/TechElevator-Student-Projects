<template>
    <div id="homeContainer">
      <img id="logo" src="@/assets/bogeyBusterWhiteLogo.png" alt width="200" height="200" />
        <div class="review1">
          <h3>To Catch A Cheater...</h3>
          <p>I always knew my father-in-law cheated at golf, now with Bogey Buster I can finally prove it! Thanks Bogey Buster!</p>
        </div>
        <div class="review2">
          <h3>I Am Truly The Best!</h3>
          <p>Thanks to BogeyBuster's leaderboard my friends are constantly reminded of my greatness and their ineptitude.</p>
        </div>
        <div class="review3">
          <h3>Observer Report</h3>
          <p>Easy to use and easy to hook up with friends! Would recommend.</p>
        </div>
        <div class="info">
          <h3>BOGEY BUSTER</h3>
          <p>Bogey Buster is a web application that allows you to really elevate your golf game. You can keep track of your scores at any golf course in
            our database and watch your score improve. We are adding golf courses every day to help cater to everyone. You can also create leagues to compete
            against your friends. The league organizer will be able to create matches between two league members and then enter their scores to ensure fairness
            and accuracy. If you want to take your game to the next level or just show off to your friends, Bogey Buster is the choice for you.<br />
            <br />
            Love, Bogey Buster Creators
          </p>
        </div>
        <div class="tigerReview">
          <h3>Tiger Woods Uses Bogey Buster!</h3>
          <p>Tiger Woods downloaded Bogey Buster before his amazing win at The Masters. The ability to track his scores allowed him to know
            which holes he needed to work on, thus elevating his game.
          </p>
          <p style="font-size: 10px">* May or may not actually be THE Tiger Woods.</p>
        </div>
      <b-carousel
        id="carousel-1"
        v-model="slide"
        :interval="7000"
        img-width="1024"
        img-height="480"
        background="#ababab"
        style="text-shadow: 1px 1px 2px #333;"
        @sliding-start="onSlideStart"
      >
      <b-carousel-slide img-src="https://i.postimg.cc/prSZsYFf/golf-Course1-Seaside-Golf-Scotland.jpg">
      </b-carousel-slide>

      <b-carousel-slide img-src="https://i.postimg.cc/3JDCKGGq/golf-Course2-Seaside-Golf-Spain.jpg">
      </b-carousel-slide>

      <b-carousel-slide img-src="https://i.postimg.cc/SxFLjK7x/golf-Course3-Kingsmill-Williamsburg-Virginia.jpg">
      </b-carousel-slide>

      <b-carousel-slide img-src="https://i.postimg.cc/kgvjm83m/golf-Course4-The-Quarry-Alberta.jpg">
      </b-carousel-slide>

      <b-carousel-slide img-src="https://i.postimg.cc/qB6j7WVQ/golf-Course5-Pebble-Beach.jpg">
      </b-carousel-slide>
    </b-carousel>
      </div>
</template>

<script>
  export default {
    data() {
      return {
        slide: 0,
        sliding: null,
      }
    },
    methods: {
      onSlideStart(slide) {
        this.sliding = true
      }
  },
  }
</script>

<style>
#homeContainer {
  width: 100%;
  height: 100%;
  max-width: 95%;
  padding: 15px;
  margin: auto;
  margin-top: 4%;
  margin-bottom: 4%;
  text-align: center;
  color: white;
  background-color: rgba(5, 5, 5, 0.671);
  border-radius: 10px;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-template-rows: auto;
  grid-row-gap: 20px;
  grid-column-gap: 20px;
  grid-template-areas: 
    "review1 logo info"
    "review2 carousel info"
    "review3 tigerReview info";

}

#logo {
  grid-area: logo;
  justify-self: center;
  align-self: center;
}

#carousel-1 {
  grid-area: carousel;
  justify-self: center;
  align-self: center;
  width: 600px;
}

.info {
  grid-area: info;
  justify-self: center;
  align-self: center;
}

.review1 {
  grid-area: review1;
  justify-self: center;
  align-self: center;
}

.review2 {
  grid-area: review2;
  justify-self: center;
  align-self: center;
}

.review3 {
  grid-area: review3;
  justify-self: center;
  align-self: center;
}

.tigerReview {
  grid-area: tigerReview;
  justify-self: center;
  align-self: center;
}

p {
  line-height: 26pt;
}

</style>
