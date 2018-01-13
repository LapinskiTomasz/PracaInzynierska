const vm = new Vue({
  el: '#app',
  data: {
    result:null,
    pb95Price:0,
    pb98Price:0,
    onPrice:0,
    lpgPrice:0,
    updateMesage:"",
    comments: [],
    viewComment:false,
    stars:0,

  },
  mounted () {
    this.getStation();
    comments =[];
    this.viewComment=false;
  },
  methods: {
    getStation(){
      token = localStorage.getItem("StationToken");
      if(token === null){
        window.location.href = 'stationLogin.html';
      }else{
      axios.get('http://localhost:8081/sapi/station',{headers: {'Session-Token': token}}).then((response) => {
        this.result = response.data;
        this.pb95Price = this.result.pb95Price;
        this.pb98Price = this.result.pb98Price;
        this.onPrice = this.result.onprice
        this.lpgPrice = this.result.lpgprice;
        this.stars = Math.round(this.result.rating);
      }).catch( error => { console.log(error); });
    }
    },
    setPrices(pb95p,pb98p,onp,lpgp){
      axios.post('http://localhost:8081/sapi/station/price',{pb98:pb98p, on:onp, lpg:lpgp, pb95:pb95p}, {headers: {'Session-Token': token}}).then((response) =>{
        console.log(response.data);
        if(response.data.code == 200){
          this.updateMesage = "Zaktualizowano ceny paliw";
        }
        else{
          this.updateMesage = "Coś poszło nie tak, spróbuj się skontaktowac z administratorem";

        }
      }).catch( error => { console.log(error); });
    },
    getComments(){
      this.comments = this.result.comments;
      this.viewComment = true;
    },

    closeComments(){
      this.comments = [];
      this.viewComment = false;
    },

    logout(){
      localStorage.removeItem("StationToken");
      window.location.href = 'stationLogin.html';
    }
  }
  });
