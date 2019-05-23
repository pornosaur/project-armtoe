public class IRMock {

	public static void main(String[] args) {
		int [] game =
			   {0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,
				0,0,0,0,0,0,0};


		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < game.length; i++) {
			sb.append(game[i]).append(",");
		}
		sb.deleteCharAt(sb.length()-1);

		System.out.println(sb.toString());
	}
}
