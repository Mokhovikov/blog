$(document).ready(function(){
    /*wysiwyg*/
    /* Liste des smileys */
    var smileys = [
        "fa fa-smile-o",
        "fa fa-fort-awesome",
        "fa fa-bed",
        "fa fa-beer",
        "fa fa-car"
    ];

    jQuery.each( smileys, function( i, val ) {
        $(".newPost").each(function(){
            $(this).find(".smileys").append('<button type="button" class="functiondata" data-func="insertHtml" data-funcdat=\'<i class="'+val+'">\'><i class="'+val+'"></i></button>');
        });

    });
    /*   Execution de commande basic */
    $('.function').click(function(){
        document.execCommand( $(this).data('func'), false );
        var content = $(this).parents(".newPost").find(".editor").html();
        $(this).parents(".newPost").find(".texthidd").val(content);
    });
    /*  Execution de commande avec données */
    $('.functiondata').click(function(){
        document.execCommand( $(this).data('func'), false , $(this).data('funcdat'));
        var content = $(this).parents(".newPost").find(".editor").html();
        $(this).parents(".newPost").find(".texthidd").val(content);
    });
    /*  Execution de commande avec lien */
    $('.functionlinkto').click(function(){
        document.execCommand( $(this).data('func'), false , $(this).parents(".newPost").find(".link").val());
        $(this).parents(".newPost").find(".linkform").slideToggle();
        var content = $(this).parents(".newPost").find(".editor").html();
        $(this).parents(".newPost").find(".texthidd").val(content);
    });

    /* Exécution de commande avec option */
    ('.functionchange').change(function(){
        var $value = $(this).find(':selected').val();
        document.execCommand( $(this).data('func'), false, $value);
        var content = $(this).parents(".newPost").find(".editor").html();
        $(this).parents(".newPost").find(".texthidd").val(content);
    });
    /* Synchronisation avec le textarea */
    $('.editor').focus(function(){
        $(this).keyup(function() {
            var content = $(this).html();
            $(this).parent(".newPost").find(".texthidd").val(content);
        });
    });
    $('.texthidd').focus(function(){
        $(this).keyup(function() {
            var content = $(this).val();
            $(this).parent(".newPost").find(".editor").html(content);
        });
    });
    /* Visualisation du code source (textarea) */
    $('.functioncode').click(function(){
        if($(this).parents(".newPost").find(".editor").is(":visible")){
            $(this).parents(".newPost").find(".editor").fadeToggle( "fast", function() {
                $(this).parents(".newPost").find(".texthidd").fadeToggle();
            });
        }
        else{
            $(this).parents(".newPost").find(".texthidd").fadeToggle( "fast", function() {
                $(this).parents(".newPost").find(".editor").fadeToggle();
            });
        }
        $(this).parents(".newPost").find(".tools").fadeToggle();
    });
    /* Affichage smileys */
    $(".functionicon").click(function(){
        $(this).parents(".newPost").find(".smileys").slideToggle();
    });
    /* Affichage lien */
    $(".functionlink").click(function(){
        $(this).parents(".newPost").find(".linkform").slideToggle();
    });

});