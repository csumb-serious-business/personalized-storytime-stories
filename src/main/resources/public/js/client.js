/* this file will contain general purpose client javascript for use across all pages in the site */

$(document).ready(() => {
    // disable forms globally -- we use AJAX
    $('form').submit(e => e.preventDefault());

    // always close other modals when opening a new one
    // from https://stackoverflow.com/a/32162883
    $('.modal').on('show.bs.modal', function () {
        $('.modal').not($(this)).each(function () {
            $(this).modal('hide');
        });
    });
});