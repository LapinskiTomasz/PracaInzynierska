
function buildPriceUrl (city,fuel) {
  return "http://localhost:8080/api/compare/price" + "?city="+city+"&fuel="+fuel
}
function buildRatingUrl (city) {
  return "http://localhost:8080/api/compare/rating" + "?city="+city
}

const vm = new Vue({
  el: '#app',
  data: {
    results: [],
    isLogged:false,
    byRating:false,
    emptyLoad:false,
    fuel:"PB95"
  },
  mounted () {
    if(localStorage.getItem('token')!=null) this.isLogged=true;
    sessionStorage.removeItem('stationID');
    this.emptyLoad = false;
  },
  methods: {
    getPosts(city,fuel) {
      let url
      if(this.byRating){
         url = buildRatingUrl(city);
        console.log("PO OCENIE");
      }
      else{
         url = buildPriceUrl(city,fuel);
        console.log("PO CENIE");
      }

      axios.get(url).then((response) => {
        this.results = response.data;
        if(Object.keys(this.results).length === 0){
          this.emptyLoad = true;
        }
      }).catch( error => { console.log(error); });
    },
    login(){
      window.location.href = 'login.html';
    },
    logout(){
      localStorage.removeItem('token');
      this.isLogged=false;
    },
    getComments(id){
      console.log(id);
      sessionStorage.setItem('stationID', id);
      window.location.href = 'comments.html';
    },
    register(){
      window.location.href = 'register.html';
    }
  }
});
