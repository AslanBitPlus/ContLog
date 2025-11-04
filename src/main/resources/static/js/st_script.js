document.addEventListener('DOMContentLoaded', function() {
        // Функция для фильтрации таблицы
        function filterTable() {
            var input, filter, table, tr, td, i, j;
            input = document.getElementById("searchInput");
            filter = input.value.toUpperCase();
            table = document.getElementById("usersTable");
            tr = table.getElementsByTagName("tr");

            // Проходим по всем строкам таблицы
            for (i = 0; i < tr.length; i++) {
                tr[i].style.display = "";
                tds = tr[i].getElementsByTagName("td");

                // Проверяем все ячейки строки
                for (j = 0; j < tds.length; j++) {
                    if (tds[j].innerHTML.toUpperCase().indexOf(filter) > -1) {
                        tr[i].style.display = "";
                        break;
                    } else {
                        tr[i].style.display = "none";
                    }
                }
            }
        }

        // Добавляем обработчик события на ввод в поле поиска
        document.getElementById("searchInput").addEventListener('keyup', function() {
            filterTable();
        });
    });