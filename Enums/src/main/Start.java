package main;

public class Start {

	public static void main(String[] args) {
		Card card = new Card(CardSuit.HEART, CardValue.EIGHT);

		CardSuit suit;
		suit = CardSuit.DIAMOND;

//		suit = 3; -> invalid

		if (suit == CardSuit.HEART) {
			System.out.println("Heart");
		} else {
			System.out.println("Something else 1");
		}

		if (suit.equals(CardSuit.HEART)) {
			System.out.println("Heart");
		} else {
			System.out.println("Something else 2");
		}

		switch (suit) {
		case CardSuit.DIAMOND:
			System.out.println("Diamond");
			break;
		case CardSuit.HEART:
			System.out.println("Heart");
			break;
		case CardSuit.SPADE:
			System.out.println("Spade");
			break;
		case CardSuit.CLUB:
			System.out.println("Club");
		default:
			System.out.println("Unknown");
			break;
		}
		
		for (CardSuit currentSuit: CardSuit.values()) {
			System.out.println(currentSuit.ordinal() + ": " + currentSuit);
		}
		
		CardValue suit2 = CardValue.KING;
		System.out.println(suit2.getValue());
		
		Desserts dessert = Desserts.BULGARIA;
		System.out.print("Name of the dessert: ");
		System.out.println(dessert.getDessert());
		
		System.out.print("Rating of the current dessert: ");
		System.out.println(dessert.getRating());
	}
}
