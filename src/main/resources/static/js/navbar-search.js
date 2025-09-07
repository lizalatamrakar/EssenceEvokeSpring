document.addEventListener("DOMContentLoaded", () => {
    const searchBar = document.getElementById("searchBar");
    const searchResults = document.getElementById("searchResults");
    searchResults.style.width = "70%";

    if (!searchBar || !searchResults) {
        console.warn("Search bar or results container not found in DOM.");
        return;
    }

    let debounceTimer;

    searchBar.addEventListener("input", () => {
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(async () => {
            const keyword = searchBar.value.trim();
            if (keyword.length < 2) {
                searchResults.style.display = "none";
                return;
            }

            try {
                console.log("Calling API:", `/search?keyword=${encodeURIComponent(keyword)}`);
                const response = await fetch(`/search?keyword=${encodeURIComponent(keyword)}`);
                if (!response.ok) throw new Error(`HTTP error: ${response.status}`);

                const products = await response.json();

                searchResults.innerHTML = "";

                if (products.length > 0) {
                    products.forEach(p => {
                        const item = document.createElement("div");
                        item.innerHTML = `
                            <a href="/productdisplay/${p.id}"
                               style="display:flex; align-items:center; text-decoration:none; color:black; padding:8px;">
                                <img src="${p.image}"
                                     style="width:40px; height:40px; object-fit:cover; margin-right:10px;">
                                <span>${p.name}</span>
                            </a>
                        `;
                        searchResults.appendChild(item);
                    });
                    searchResults.style.display = "block";
                } else {
                    searchResults.innerHTML = "<div style='padding:8px;'>No products found</div>";
                    searchResults.style.display = "block";
                }
            } catch (err) {
                console.error("Search error:", err);
            }
        }, 300); // debounce to reduce API calls
    });

    // Hide results when clicking outside
    document.addEventListener("click", (e) => {
        if (!searchBar.contains(e.target) && !searchResults.contains(e.target)) {
            searchResults.style.display = "none";
        }
    });
});
