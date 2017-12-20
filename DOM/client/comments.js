
const BaseUrl = "http://localhost:8080/api/comments";


function buildUrl (stationID) {
  return BaseUrl + "?stationID="+stationID
}

Vue.component('modal', {
  template: '#modal-template'
})

const vm = new Vue({
  el: '#app',
  data: {
    comments: [],
    isLogged:false,
    showModal: false,
    rate:1,
    text:""
  },
  mounted () {
    if(localStorage.getItem('token')!=null) this.isLogged=true;
    this.getComments(sessionStorage.getItem('stationID'));
  },
  methods: {
    getComments(id){
      let url = buildUrl(id);
      axios.get(url).then((response) => {
        this.comments = response.data;
      }).catch( error => { console.log(error); });
    },
    addComment(text,rate){
      station = sessionStorage.getItem('stationID');
      token = localStorage.getItem('token');
      axios.post('http://localhost:8080/api/login/comment',{stationID:station, comment:text, rating:rate},{headers: {'Session-Token': token}}).then((response) =>{
        console.log(response.data);
        this.showModal = true;
        this.getComments(sessionStorage.getItem('stationID'));
        this.text="";
        this.rate =1;
    }).catch( error => { console.log(error); });
  },
  refresh(){
    window.location.href = 'comments.html';
  }

}
});
