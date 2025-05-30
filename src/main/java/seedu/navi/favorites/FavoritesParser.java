package seedu.navi.favorites;

import seedu.navi.textui.NaviTextUi;

import java.util.Scanner;

/**
 * Handles parsing and executing user commands for the Favorites feature.
 * Supports adding, removing, viewing, sorting, searching, and undoing actions on favorite items.
 */
public class FavoritesParser {
    private static Favorites favorites;
    private Scanner scanner;

    /**
     * Constructs a FavoritesParser with the given Favorites object.
     *
     * @param favorites The Favorites object to manipulate.
     */
    public FavoritesParser(Favorites favorites) {
        assert favorites != null : "Favorites object cannot be null";
        this.favorites = favorites;
    }

    /**
     * Starts the command loop for interacting with the Favorites feature.
     * Accepts user commands and performs appropriate actions until 'exit' is entered.
     */
    public void start() {
        scanner = new Scanner(System.in);
        NaviTextUi.printLineSeparator();
        System.out.println("Favorites Tracker: Enter a command:");
        System.out.println("  - add <description> : <rating> : <location>");
        System.out.println("  - remove N");
        System.out.println("  - view");
        System.out.println("  - sort asc");
        System.out.println("  - sort desc");
        System.out.println("  - search X");
        System.out.println("  - undo");
        System.out.println("  - exit");
        NaviTextUi.printLineSeparator();

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                NaviTextUi.printLineSeparator();
                System.out.println("Please enter a command.");
                NaviTextUi.printLineSeparator();
                continue;
            }

            String[] parts = input.split(" ", 2);
            String command = parts[0].toLowerCase();
            String arguments = (parts.length > 1) ? parts[1].trim() : "";

            switch (command) {
            case "add":
                handleAddCommand(arguments);
                break;
            case "remove":
                handleRemoveCommand(arguments);
                break;
            case "view":
                favorites.viewFavorites();
                break;
            case "sort":
                handleSortCommand(arguments);
                break;
            case "search":
                favorites.searchFavorites(arguments);
                NaviTextUi.printLineSeparator();
                break;
            case "undo":
                favorites.undoRemove();
                break;
            case "exit":
                NaviTextUi.printLineSeparator();
                System.out.println("Exiting Favorites Tracker.");
                NaviTextUi.printLineSeparator();
                return;
            default:
                NaviTextUi.printLineSeparator();
                System.out.println("Unknown command. Try: add description : rating : location, remove N, view, " +
                        "sort asc/desc, search X, undo, exit.");
                NaviTextUi.printLineSeparator();
            }
        }
    }

    /**
     * Handles the 'add' command.
     * Adds a new favorite item if the input format is correct and no duplicate exists.
     *
     * @param arguments The arguments string containing description, rating, and location.
     */
    private static void handleAddCommand(String arguments) {
        String[] params = arguments.split("\\s*:\\s*");

        if (params.length != 3) {
            NaviTextUi.printLineSeparator();
            System.out.println("Invalid format. Use: add <description> : <rating> : <location>");
            NaviTextUi.printLineSeparator();
            return;
        }

        try {
            NaviTextUi.printLineSeparator();
            String description = params[0].trim();
            String category = params[2].trim();

            if (description.isEmpty() || category.isEmpty()) {
                System.out.println("Description and location cannot be empty.");
                NaviTextUi.printLineSeparator();
                return;
            }

            if (!params[1].trim().matches("\\d+")) {
                System.out.println("Rating must be a valid integer (no decimals allowed).");
                NaviTextUi.printLineSeparator();
                return;
            }

            int rating = Integer.parseInt(params[1].trim());

            if (rating < 0) {
                System.out.println("Rating cannot be negative.");
                NaviTextUi.printLineSeparator();
                return;
            }

            boolean isDuplicate = false;
            for (String item : favorites.getFavoriteItems()) {
                String[] itemDetails = item.split("\\|");
                String itemDescription = itemDetails[0].trim();
                String itemRatingString = itemDetails[1].trim().replace("Rating: ", "");
                String itemCategory = itemDetails[2].trim().replace("Location: ", "");
                int itemRating = Integer.parseInt(itemRatingString);

                if (itemDescription.equalsIgnoreCase(description) && itemRating == rating &&
                        itemCategory.equalsIgnoreCase(category)) {
                    isDuplicate = true;
                    break;
                }
            }

            if (isDuplicate) {
                System.out.println("This favorite already exists. Duplicate entries are not allowed.");
            } else {
                favorites.addFavorite(description, rating, category);
            }

            NaviTextUi.printLineSeparator();
        } catch (NumberFormatException e) {
            System.out.println("Rating must be a valid number.");
            NaviTextUi.printLineSeparator();
        }
    }

    /**
     * Handles the 'remove' command.
     * Removes a favorite item at the specified index.
     *
     * @param arguments The index argument as a string.
     */
    private void handleRemoveCommand(String arguments) {
        try {
            int index = Integer.parseInt(arguments.trim()) - 1;
            favorites.removeFavorite(index);
        } catch (NumberFormatException e) {
            NaviTextUi.printLineSeparator();
            System.out.println("Please enter a valid index number.");
            NaviTextUi.printLineSeparator();
        }
    }

    /**
     * Handles the 'sort' command.
     * Sorts the favorite items in ascending or descending order based on rating.
     *
     * @param arguments The sort order argument ('asc' or 'desc').
     */
    private void handleSortCommand(String arguments) {
        if (arguments.equalsIgnoreCase("asc")) {
            favorites.sortFavorites(false);
        } else if (arguments.equalsIgnoreCase("desc")) {
            favorites.sortFavorites(true);
        } else {
            System.out.println("Invalid sort command. Use: sort asc OR sort desc");
        }
    }
}
