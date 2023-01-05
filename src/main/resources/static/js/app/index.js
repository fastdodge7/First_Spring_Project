var index = {
    init: function() {
        var _this = this;
        $('#btn-save').on('click', function() {
            _this.save();
        });

        // btn-update라는 id를 가진 HTML 엘리먼트에 click 이벤트가 발생하면 이 객체의 update 함수를 실행하라는 의미
        $('#btn-update').on('click', function() {
            _this.update();
        });
    },
    save : function() {
        var data = {
            title: $('#title').val(),
            author: $('#author').val(),
            content: $('#content').val()
        };

        $.ajax({ // jQuery.ajax와 같은 의미. $문자가 jQuery를 지칭하는 식별자임.
            type: 'POST',
            url: '/api/v1/posts',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('글이 등록되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    update : function() {
        var data = {
            // id: $('#id').val(),
            title: $('#title').val(),
            // author: $('#author').val(),
            content: $('#content').val()
        };

        // Update 요청시에, Request body에는 제목과 내용 정보만 들어가므로, id를 따로 분리해야 함.
        var id = $("#id").val();

        $.ajax({ // jQuery.ajax와 같은 의미. $문자가 jQuery를 지칭하는 식별자임.
            type: 'PUT',
            url: '/api/v1/posts/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('글이 수정되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

index.init();