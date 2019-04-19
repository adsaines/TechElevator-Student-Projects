<template>
    <div class="map"/>
</template>

<script>

import gmapsInit from '@/gmaps';

export default {
  name: 'Map',
  props: {
      courseArray: Array
  },
  data() {
      return {
          mapMarkers: [],
          geocoder: null,
          map: null,
          google: null
      }
  },
  async mounted() {
    try {
      this.google = await gmapsInit();
      this.geocoder = new this.google.maps.Geocoder();
      this.map = new this.google.maps.Map(this.$el);

      this.geocoder.geocode({ address: 'Cincinnati' }, (results, status) => {
        if (status !== 'OK' || !results[0]) {
          throw new Error(status);
        }

        this.map.setCenter(results[0].geometry.location);
        this.map.fitBounds(results[0].geometry.viewport);
      });

    //   const markers = geocodedCourseArray.map(x => new google.maps.Marker({ ...x, map }));
    } catch (error) {
      console.error(error);
    }
  },
  watch: {
    courseArray(oldValue, newValue) {
    
        let google = this.google;
        let map = this.map;

        for (let i = 0; i < this.mapMarkers.length; i++) {
          this.mapMarkers[i].setMap(null);
        }

        this.mapMarkers = [];

        let mapMarkers = this.mapMarkers;
        
        for (let i = 0; i < this.courseArray.length; i++) { 
            let courseName = this.courseArray[i].courseName;
            this.geocoder.geocode({ address: this.courseArray[i].courseName, address: '' + this.courseArray[i].zipCode }, (results, status) => {
                if (status == 'OK') {
                    mapMarkers.push(new google.maps.Marker({position: results[0].geometry.location, map: map, title: courseName }));
                }
            });
        }
    }
  }
};
</script>

<style>
html,
body {
  margin: 0;
  padding: 0;
}

.map {
  width: 100vw;
  height: 100vh;
}
</style>
