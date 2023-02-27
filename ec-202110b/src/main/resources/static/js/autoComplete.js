$(function(){
   

   
    $.ajax({
        /** オートコンプリート用のリストを取得するURL*/
        url: "http://localhost:8080/ec-202110b/autoComplete",
        dataType: "json",
        type: 'GET'
    }).then(function(searchResult){ /** 検索結果のリストは引数に自動で入る*/
   　　　　
        $('#code').autocomplete({ /** 実装したい入力欄のid/classを指定*/
            source : searchResult, /** 候補リスト(必須)*/
            autoFocus: true, /**リスト表示に際してリスト先頭にフォーカスするか*/
            delay: 500, /**キー入力からリスト表示までの遅延時間（ミリ秒）*/
            minLength: 1 /**オートコンプリート機能が働く最小文字数*/
        });
        },function(){
    }).fail(function(XMLHttpRequst,textStatus,errorThrown){
    console.log(XMLHttpRequest.status);
    });
    
});