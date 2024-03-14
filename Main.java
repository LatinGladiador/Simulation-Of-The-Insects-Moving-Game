import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * The Main class represents the entry point for the insect simulation program. It reads input from a file,
 * initializes the game board, processes insects and food points, simulates movement, and writes the results
 * to an output file.
 */
public class Main {
    // Lists to store existing insects, existing positions, and all insects
    private static final List<String> EXISTING_INSECTS  = new ArrayList<>();
    private static final List<String> EXISTING_POSITIONS  = new ArrayList<>();
    private static final List<Insect> ALL_INSECTS  = new ArrayList<>();
    private static Board gameBoard;

    // Constants for defining constraints on board size and number of entities
    private static final int MIN_BOARD_SIZE = 4;
    private static final int MAX_BOARD_SIZE = 1000;
    private static final int MIN_NUM_INSECTS = 1;
    private static final int MAX_NUM_INSECTS = 16;
    private static final int MIN_NUM_FOOD_POINTS = 1;
    private static final int MAX_NUM_FOOD_POINTS = 200;

    // Indices for extracting X and Y positions from input
    private static final int INDEX_X_POSITION = 2;
    private static final int INDEX_Y_POSITION = 3;
    /**
     *
     * Main
     *
     * @param args  the args.
     */
    public static void main(String[] args) {
        FileOutputStream output;
        try {
            output = new FileOutputStream("output.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            //Read input from file
            Scanner scanner = new Scanner(new File("input.txt"));

            //Read board size, number of insects, and number of food points
            int boardSize = Integer.parseInt(scanner.nextLine());
            int numInsects = Integer.parseInt(scanner.nextLine());
            int numFoodPoints = Integer.parseInt(scanner.nextLine());

            //Invalid board size
            if (boardSize < MIN_BOARD_SIZE  || boardSize > MAX_BOARD_SIZE) {
                throw new InvalidBoardSizeException();
            }

            //Invalid number of insects and foot points
            if (numInsects < MIN_NUM_INSECTS  || numInsects > MAX_NUM_INSECTS) {
                throw new InvalidNumberOfInsectsException();
            }

            if (numFoodPoints < MIN_NUM_FOOD_POINTS  || numFoodPoints > MAX_NUM_FOOD_POINTS) {
                throw new InvalidNumberOfFoodPointsException();
            }

            //Initialize the game board
            gameBoard = new Board(boardSize);

            //Process insects
            for (int i = 0; i < numInsects; i++) {

                String input = scanner.nextLine();
                String[] allParts = input.split(" ");


                int x = Integer.parseInt(allParts[INDEX_X_POSITION]);
                int y = Integer.parseInt(allParts[INDEX_Y_POSITION]);

                //Check insect color
                InsectColor color = InsectColor.RED.toColor(allParts[0]);

                //Check insect type
                Type type = Type.ANT.toType(allParts[1]);

                //Invalid insect position
                if (x < 1 || x > boardSize || y < 1 || y > boardSize) {
                    throw new InvalidEntityPositionException();
                }

                //Create and add the insect to the board
                Insect insect = null;
                switch (type) {
                    case ANT:
                        insect = new Ant(color, new EntityPosition(x, y));
                        break;
                    case BUTTERFLY:
                        insect = new Butterfly(color, new EntityPosition(x, y));
                        break;
                    case SPIDER:
                        insect = new Spider(color, new EntityPosition(x, y));
                        break;
                    case GRASSHOPPER:
                        insect = new Grasshopper(color, new EntityPosition(x, y));
                        break;
                    default:
                }

                //Duplicate insects
                String insectKey = allParts[0] + "-" + allParts[1];
                if (EXISTING_INSECTS .contains(insectKey)) {
                    throw new DuplicateInsectException();
                }

                String position = insect.getPosition().toString();
                if (EXISTING_POSITIONS .contains(position)) {
                    throw new TwoEntitiesOnSamePositionException();
                }
                gameBoard.addEntity(insect);
                EXISTING_INSECTS .add(insectKey);
                EXISTING_POSITIONS .add(position);
                ALL_INSECTS .add(insect);
            }
            //Process food points
            for (int i = 0; i < numFoodPoints; i++) {
                String input = scanner.nextLine();
                String[] allParts = input.split(" ");

                int x = Integer.parseInt(allParts[1]);
                int y = Integer.parseInt(allParts[2]);

                // Invalid entity position
                if (x < 1 || x > boardSize || y < 1 || y > boardSize) {
                    throw new InvalidEntityPositionException();
                }

                // Create and add the food point to the board
                FoodPoint foodPoint = new FoodPoint(Integer.parseInt(allParts[0]), new EntityPosition(x, y));

                // Two entities in the same position
                String position = foodPoint.getPosition().toString();
                if (EXISTING_POSITIONS .contains(position)) {
                    throw new TwoEntitiesOnSamePositionException();
                }

                gameBoard.addEntity(foodPoint);
                EXISTING_POSITIONS .add(position);
            }

            scanner.close();

            //Simulation: Simulate movement for each insect and write to output
            for (Insect insect : ALL_INSECTS) {
                simulateInsectMovement(insect, output);
            }
        } catch (Exception e) {
            // Handle exceptions by writing the error message to the output file
            try {
                output.write(e.getMessage().getBytes());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Simulates the movement of an insect and writes the results to the output stream.
     *
     * @param insect The insect to simulate movement for.
     * @param output The FileOutputStream to write the results to.
     */
    private static void simulateInsectMovement(Insect insect, FileOutputStream output) {
        Direction direction = gameBoard.getDirection(insect);
        // Call getDirectionSum method
        int directionSum = gameBoard.getDirectionSum(insect);
        String string = insect.getColor().getTextRepresentation() + ' ' + insect.getType()
                .getTextRepresentation() + ' ' + direction.getTextRepresentation() + ' ' + directionSum + '\n';
        try {
            output.write(string.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
/**
 * The Board class represents the game board and contains methods for managing entities on the board.
 */
class Board {

    private final Map<String, BoardEntity> boardData;
    private final int size;
    /**
     *
     * Board
     *
     * @param boardSize  the board size.
     * @return public
     */
    public Board(int boardSize) {
        this.size = boardSize;
        this.boardData = new HashMap<>();
    }
    /**
     * Adds a BoardEntity to the board.
     *
     * @param entity The BoardEntity to be added to the board.
     */
    public void addEntity(BoardEntity entity) {
        this.boardData.put(entity.getPosition().toString(), entity);
    }
    /**
     * Gets the BoardEntity at a specified position.
     *
     * @param position The EntityPosition representing the position to retrieve the entity from.
     * @return The BoardEntity at the specified position, or null if no entity is found.
     */
    public BoardEntity getEntity(EntityPosition position) {
        return this.boardData.get(position.toString());
    }
    /**
     * Gets the best direction for an insect to move.
     *
     * @param insect The Insect for which to determine the best direction.
     * @return The Direction representing the best direction for the insect to move.
     */
    public Direction getDirection(Insect insect) {
        return insect.getBestDirection(boardData, size);
    }
    /**
     * Gets the sum of values when an insect travels in its best direction.
     *
     * @param insect The Insect for which to calculate the direction sum.
     * @return The sum of values when the insect travels in its best direction.
     */
    public int getDirectionSum(Insect insect) {
        Direction direction = getDirection(insect);
        return insect.travelDirection(direction, boardData, size);
    }
    /**
     * Gets the entire board data as a map of positions to entities.
     *
     * @return A map representing the entire board data.
     */
    public Map<String, BoardEntity> getBoardData() {
        return boardData;
    }
}
/**
 * The BoardEntity class is an abstract class representing entities on the game board.
 */
abstract class BoardEntity {

    private EntityPosition position;
    /**
     *
     * Board entity
     *
     * @param position  the position.
     * @return public
     */
    public BoardEntity(EntityPosition position) {
        this.position = position;
    }
    /**
     * Gets the direction of the entity. Default implementation returns null.
     *
     * @return The direction of the entity, or null if not applicable.
     */
    public Object getDirection() {
        return null;
    }
    /**
     * Gets the position of the entity.
     *
     * @return The EntityPosition representing the position of the entity.
     */
    public EntityPosition getPosition() {
        return position;
    }
    /**
     * Sets the position of the entity.
     *
     * @param position The new EntityPosition for the entity.
     */
    public void setPosition(EntityPosition position) {
        this.position = position;
    }
}
/**
 * The EntityPosition class represents the position of an entity on the game board.
 */
class EntityPosition {
    /**
     *
     * Gets the X
     *
     * @return the  X
     */
    public int getX() {
        return x;
    }
    /**
     *
     * Sets the X
     *
     * @param x  the x.
     */
    public void setX(int x) {
        this.x = x;
    }
    /**
     *
     * Gets the Y
     *
     * @return the  Y
     */
    public int getY() {
        return y;
    }
    /**
     *
     * Sets the Y
     *
     * @param y  the y.
     */
    public void setY(int y) {
        this.y = y;
    }

    private int x;
    private int y;

    /**
     *
     * Entity position
     *
     * @param x  the x.
     * @param y  the y.
     * @return public
     */
    public EntityPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Moves the position in a specified direction.
     *
     * @param direction The Direction in which to move the position.
     * @return A new EntityPosition representing the moved position.
     */
    public EntityPosition move(Direction direction) {
        int newX = x;
        int newY = y;

        switch (direction) {
            case N:
                newX--;
                break;
            case E:
                newY++;
                break;
            case S:
                newX++;
                break;
            case W:
                newY--;
                break;
            case NE:
                newY++;
                newX--;
                break;
            case SE:
                newY++;
                newX++;
                break;
            case SW:
                newY--;
                newX++;
                break;
            case NW:
                newY--;
                newX--;
                break;
            default:
        }

        return new EntityPosition(newX, newY);
    }

    /**
     * Checks if the position is valid within the given board size.
     *
     * @param size The size of the game board.
     * @return True if the position is valid, false otherwise.
     */
    public boolean isValidPosition(int size) {
        return (x >= 1 && x <= size && y >= 1 && y <= size);
    }
    @Override
    /**
     * Returns a string representation of the position.
     *
     * @return A string representation of the position in the format [x, y].
     */
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
/**
 * The FoodPoint class represents a food point on the game board.
 */
class FoodPoint extends BoardEntity {

    private final int value;
    /**
     *
     * Food point
     *
     * @param value  the value.
     * @param position  the position.
     * @return public
     */
    public FoodPoint(int value, EntityPosition position) {
        super(position);
        this.value = value;
    }

    /**
     * Gets the value of the food point.
     *
     * @return The value of the food point.
     */
    public int getValue() {
        return value;
    }
    /**
     * Overrides the getPosition method from BoardEntity.
     *
     * @return The EntityPosition representing the position of the food point.
     */
    @Override
    public EntityPosition getPosition() {
        return super.getPosition();
    }

}
/**
 * The Insect class is an abstract class representing insects on the game board.
 */
abstract class Insect extends BoardEntity {

    protected final InsectColor color;
    protected final Type type;
    protected Direction direction;
    protected List<Direction> validDirections;
    /**
     *
     * Insect
     *
     * @param color  the color.
     * @param type  the type.
     * @param position  the position.
     * @return public
     */
    public Insect(InsectColor color, Type type, EntityPosition position) {
        super(position);
        this.color = color;
        this.type = type;
        this.direction = Direction.N;
        this.validDirections = getDefaultValidDirections(type);
    }
    /**
     * Gets the valid directions for the insect.
     *
     * @return A list of valid directions for the insect.
     */
    public List<Direction> getValidDirections() {
        return validDirections;
    }
    /**
     * Abstract method to get the best direction for an insect.
     *
     * @param boardData The map of positions to entities on the game board.
     * @param boardSize The size of the game board.
     * @return The Direction representing the best direction for the insect to move.
     */
    public abstract Direction getBestDirection(Map<String, BoardEntity> boarData, int boardSize);

    // Method to get default valid directions based on insect type
    private List<Direction> getDefaultValidDirections(Type type) {
        switch (type) {
            case ANT:
                return Arrays.asList(Direction.N, Direction.E, Direction.S, Direction.W,
                        Direction.NE, Direction.SE, Direction.SW, Direction.NW);
            case BUTTERFLY:
                return Arrays.asList(Direction.N, Direction.E, Direction.S, Direction.W);
            case SPIDER:
                return Arrays.asList(Direction.NE, Direction.SE, Direction.SW, Direction.NW);
            case GRASSHOPPER:
                return Arrays.asList(Direction.N, Direction.E, Direction.S, Direction.W);
            default:
                return null;
        }
    }
    /**
     *
     * Gets the color
     *
     * @param s  the s.
     * @return the color
     */
    public InsectColor getColor(String s) {
        return color;
    }
    /**
     *
     * Gets the type
     *
     * @return the type
     */
    public Type getType() {
        return type;
    }
    /**
     *
     * Sets the direction
     *
     * @param direction  the direction.
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     *
     * Gets the color
     *
     * @return the color
     */
    public InsectColor getColor() {
        return color;
    }

    // Abstract method to determine the travel direction for an insect
    public abstract int travelDirection(Direction dir, Map<String, BoardEntity> boarData, int boardSize);

}

/**
 * The Grasshopper class represents a grasshopper insect on the game board.
 */
class Grasshopper extends Insect {

    /**
     *
     * Grasshopper
     *
     * @param color  the color.
     * @param entityPosition  the entity position.
     * @return public
     */
    public Grasshopper(InsectColor color, EntityPosition entityPosition) {
        super(color, Type.GRASSHOPPER, entityPosition);
    }
    /**
     * Gets the best direction for a Grasshopper.
     *
     * @param boardData The map of positions to entities on the game board.
     * @param boardSize The size of the game board.
     * @return The Direction representing the best direction for the Grasshopper to move.
     */
    public Direction getBestDirection(Map<String, BoardEntity> boardData, int boardSize) {

        int maxFood = -1;
        Direction bestDirection = null;

        for (Direction dir : getValidDirections()) {
            int foodEaten = 0;
            EntityPosition currentPosition = getPosition().move(dir).move(dir);

            while (currentPosition.isValidPosition(boardSize)) {
                BoardEntity entity = boardData.get(currentPosition.toString());
                if (entity instanceof FoodPoint) {
                    //Grasshopper eats the food
                    foodEaten += ((FoodPoint) entity).getValue();
                }
                //Move to the next position
                currentPosition = currentPosition.move(dir).move(dir);
            }
            if (foodEaten > maxFood) {
                maxFood = foodEaten;
                bestDirection = dir;
            }
        }

        return bestDirection;
    }
    /**
     * Simulates the travel of a Grasshopper in a given direction.
     *
     * @param dir The Direction in which the Grasshopper will travel.
     * @param boardData The map of positions to entities on the game board.
     * @param boardSize The size of the game board.
     * @return The sum of values when the Grasshopper travels in the specified direction.
     */
    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int boardSize) {

        int foodEaten = 0;
        EntityPosition currentPosition = getPosition();
        currentPosition = getPosition().move(dir).move(dir);

        while (currentPosition.isValidPosition(boardSize)) {
            BoardEntity entity = boardData.get(currentPosition.toString());
            if (entity instanceof FoodPoint) {
                //Grasshopper eats the food
                foodEaten += ((FoodPoint) entity).getValue();
                //Remove the food from the board
                boardData.remove(entity.getPosition().toString());
            }

            //Check for an insect
            if (entity instanceof Insect && !((Insect) entity).getColor().equals(getColor())) {
                //Remove from the board
                boardData.remove(getPosition().toString());
                return foodEaten;
            }

            //Move to the next position
            currentPosition = currentPosition.move(dir).move(dir);
        }

        //Remove the Grasshopper from the board after it has finished its travel
        boardData.remove(getPosition().toString());

        //Update the new position
        setPosition(currentPosition);
        return foodEaten;
    }

}
/**
 * The Butterfly class represents an insect of type Butterfly that extends the Insect class
 * and implements the OrthogonalMoving interface.
 */
class Butterfly extends Insect implements OrthogonalMoving {
    /**
     * Constructor for the Butterfly class.
     *
     * @param color           The color of the Butterfly.
     * @param entityPosition  The position of the Butterfly on the board.
     */
    public Butterfly(InsectColor color, EntityPosition entityPosition) {
        super(color, Type.BUTTERFLY, entityPosition);
    }

    /**
     * Gets the best direction for the Butterfly based on visibility.
     *
     * @param boardData  The map containing board entities.
     * @param boardSize  The size of the game board.
     * @return The best direction for the Butterfly.
     */
    public Direction getBestDirection(Map<String, BoardEntity> boardData, int boardSize) {
        int maxi = -1;
        Direction bestDirection = null;
        for (Direction dir : validDirections) {
            int sum = getOrthogonalDirectionVisibleValue(dir, getPosition(), boardData, boardSize);
            if (sum > maxi) {
                maxi = sum;
                bestDirection = dir;
            }
        }
        return bestDirection;
    }
    /**
     * Simulates the travel of the Butterfly in a given direction.
     *
     * @param dir        The direction in which the Butterfly travels.
     * @param boardData  The map containing board entities.
     * @param boardSize  The size of the game board.
     * @return The amount of food eaten during the travel.
     */
    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int boardSize) {

        return travelOrthogonally(dir, getPosition(), color, boardData, boardSize);
    }

    @Override
    /**
     *
     * Gets the orthogonal direction visible value
     *
     * @param dir  the dir.
     * @param entityPosition  the entity position.
     * @param boardData  the board data.
     * @param boardSize  the board size.
     * @return the orthogonal direction visible value
     */
    public int getOrthogonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition,
                                                  Map<String, BoardEntity> boardData, int boardSize) {

        int foodEaten = 0;
        EntityPosition currentPosition = entityPosition.move(dir);

        while (currentPosition.isValidPosition(boardSize)) {
            BoardEntity entity = boardData.get(currentPosition.toString());
            if (entity instanceof FoodPoint) {
                // Insect eats the food
                foodEaten += ((FoodPoint) entity).getValue();
            }
            // Move to the next position
            currentPosition = currentPosition.move(dir);
        }

        return foodEaten;
    }
    @Override
    /**
     *
     * Travel orthogonally
     *
     * @param dir  the dir.
     * @param entityPosition  the entity position.
     * @param color  the color.
     * @param boardData  the board data.
     * @param boardSize  the board size.
     * @return int
     */
    public int travelOrthogonally(Direction dir, EntityPosition entityPosition, InsectColor color,
                                  Map<String, BoardEntity> boardData, int boardSize) {

        int foodEaten = 0;
        EntityPosition currentPosition = getPosition().move(dir);

        while (currentPosition.isValidPosition(boardSize)) {
            BoardEntity entity = boardData.get(currentPosition.toString());

            if (entity instanceof FoodPoint) {
                //Butterfly eats the food
                foodEaten += ((FoodPoint) entity).getValue();
                //Remove the food from the board
                boardData.remove(entity.getPosition().toString());
            }

            //Check for an insect
            if (entity instanceof Insect && !((Insect) entity).getColor().equals(getColor())) {
                //Remove from the board
                boardData.remove(getPosition().toString());
                return foodEaten;
            }

            //Move to the next position
            currentPosition = currentPosition.move(dir);

        }

        //Remove the Butterfly from the board after it has finished its travel
        boardData.remove(getPosition().toString());

        //Update the new position
        setPosition(currentPosition);
        return foodEaten;
    }

}
/**
 * The Ant class represents an insect of type Ant that extends the Insect class
 * and implements both the OrthogonalMoving and DiagonalMoving interfaces.
 */
class Ant extends Insect implements OrthogonalMoving, DiagonalMoving {
    /**
     * Constructor for the Ant class.
     *
     * @param color    The color of the Ant.
     * @param position The position of the Ant on the board.
     */
    public Ant(InsectColor color, EntityPosition position) {
        super(color, Type.ANT, position);
    }
    /**
     *
     * Gets the best direction
     *
     * @param boardData  the board data.
     * @param boardSize  the board size.
     * @return the best direction
     */
    public Direction getBestDirection(Map<String, BoardEntity> boardData, int boardSize) {
        int maxi = -1;
        Direction bestDirection = null;
        for (Direction dir : validDirections) {
            int sum = 0;
            switch (dir) {
                case N:
                case E:
                case S:
                case W:
                    sum = getOrthogonalDirectionVisibleValue(dir, getPosition(), boardData, boardSize);
                    break;
                case NW:
                case NE:
                case SE:
                case SW:
                    sum = getDiagonalDirectionVisibleValue(dir, getPosition(), boardData, boardSize);
                    break;
                default:
            }

            if (sum > maxi) {
                maxi = sum;
                bestDirection = dir;
            }
        }
        return bestDirection;
    }

    /**
     *
     * Travel direction
     *
     * @param dir  the dir.
     * @param boardData  the board data.
     * @param boardSize  the board size.
     * @return int
     */    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int boardSize) {
        switch (dir) {
            case N:
            case E:
            case S:
            case W:
                return travelOrthogonally(dir, getPosition(), color, boardData, boardSize);
            case NW:
            case NE:
            case SE:
            case SW:
                return travelDiagonally(dir, getPosition(), color, boardData, boardSize);
            default:
        }
        return 0;
    }

    @Override
    /**
     *
     * Gets the diagonal direction visible value
     *
     * @param dir  the dir.
     * @param entityPosition  the entity position.
     * @param boardData  the board data.
     * @param boardSize  the board size.
     * @return the diagonal direction visible value
     */
    public int getDiagonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition,
                                                Map<String, BoardEntity> boardData, int boardSize) {

        int foodEaten = 0;
        EntityPosition currentPosition = entityPosition.move(dir);

        while (currentPosition.isValidPosition(boardSize)) {
            BoardEntity entity = boardData.get(currentPosition.toString());
            if (entity instanceof FoodPoint) {
                // Insect eats the food
                foodEaten += ((FoodPoint) entity).getValue();
            }
            // Move to the next position
            currentPosition = currentPosition.move(dir);
        }

        return foodEaten;
    }

    @Override
    /**
     *
     * Travel diagonally
     *
     * @param dir  the dir.
     * @param entityPosition  the entity position.
     * @param color  the color.
     * @param boardData  the board data.
     * @param boardSize  the board size.
     * @return int
     */
    public int travelDiagonally(Direction dir, EntityPosition entityPosition, InsectColor color,
                                Map<String, BoardEntity> boardData, int boardSize) {

        int foodEaten = 0;
        EntityPosition currentPosition = getPosition().move(dir);

        while (currentPosition.isValidPosition(boardSize)) {
            BoardEntity entity = boardData.get(currentPosition.toString());

            if (entity instanceof FoodPoint) {
                //Ant eats the food
                foodEaten += ((FoodPoint) entity).getValue();
                //Remove the food from the board
                boardData.remove(entity.getPosition().toString());
            }

            //Check for an insect
            if (entity instanceof Insect && !((Insect) entity).getColor().equals(getColor())) {
                //Remove from the board
                boardData.remove(getPosition().toString());
                return foodEaten;
            }

            //Move to the next position
            currentPosition = currentPosition.move(dir);

        }

        //Remove the Ant from the board after it has finished its travel
        boardData.remove(getPosition().toString());

        //Update the new position
        setPosition(currentPosition);

        return foodEaten;
    }


    @Override
    /**
     *
     * Gets the orthogonal direction visible value
     *
     * @param dir  the dir.
     * @param entityPosition  the entity position.
     * @param boardData  the board data.
     * @param boardSize  the board size.
     * @return the orthogonal direction visible value
     */
    public int getOrthogonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition,
                                                  Map<String, BoardEntity> boardData, int boardSize) {

        int foodEaten = 0;
        EntityPosition currentPosition = entityPosition.move(dir);

        while (currentPosition.isValidPosition(boardSize)) {
            BoardEntity entity = boardData.get(currentPosition.toString());
            if (entity instanceof FoodPoint) {
                // Insect eats the food
                foodEaten += ((FoodPoint) entity).getValue();
            }
            // Move to the next position
            currentPosition = currentPosition.move(dir);
        }

        return foodEaten;
    }

    @Override
    /**
     *
     * Travel orthogonally
     *
     * @param dir  the dir.
     * @param entityPosition  the entity position.
     * @param color  the color.
     * @param boardData  the board data.
     * @param boardSize  the board size.
     * @return int
     */
    public int travelOrthogonally(Direction dir, EntityPosition entityPosition, InsectColor color,
                                  Map<String, BoardEntity> boardData, int boardSize) {

        int foodEaten = 0;
        EntityPosition currentPosition = getPosition().move(dir);

        while (currentPosition.isValidPosition(boardSize)) {
            BoardEntity entity = boardData.get(currentPosition.toString());

            if (entity instanceof FoodPoint) {
                //Ant eats the food
                foodEaten += ((FoodPoint) entity).getValue();
                //Remove the food from the board
                boardData.remove(entity.getPosition().toString());
            }

            //Check for an insect
            if (entity instanceof Insect && !((Insect) entity).getColor().equals(getColor())) {
                //Remove from the board
                boardData.remove(getPosition().toString());
                return foodEaten;
            }

            //Move to the next position
            currentPosition = currentPosition.move(dir);

        }

        //Remove the Ant from the board after it has finished its travel
        boardData.remove(getPosition().toString());

        //Update the new position
        setPosition(currentPosition);

        return foodEaten;
    }
}
/**
 * The Spider class represents an insect of type Spider that extends the Insect class
 * and implements the DiagonalMoving interface.
 */
class Spider extends Insect implements DiagonalMoving {
    /**
     * Constructor for the Spider class.
     *
     * @param color    The color of the Spider.
     * @param position The position of the Spider on the board.
     */
    public Spider(InsectColor color, EntityPosition position) {
        super(color, Type.SPIDER, position);
    }

    /**
     *
     * Gets the best direction
     *
     * @param boardData  the board data.
     * @param boardSize  the board size.
     * @return the best direction
     */
    public Direction getBestDirection(Map<String, BoardEntity> boardData, int boardSize) {

        int maxi = -1;
        Direction bestDirection = null;
        for (Direction dir : validDirections) {
            int sum = getDiagonalDirectionVisibleValue(dir, getPosition(), boardData, boardSize);
            if (sum > maxi) {
                maxi = sum;
                bestDirection = dir;
            }

        }
        return bestDirection;
    }

    /**
     *
     * Travel direction
     *
     * @param dir  the dir.
     * @param boardData  the board data.
     * @param boardSize  the board size.
     * @return int
     */
    public int travelDirection(Direction dir, Map<String, BoardEntity> boardData, int boardSize) {

        return travelDiagonally(dir, getPosition(), color, boardData, boardSize);
    }

    @Override
    /**
     *
     * Gets the diagonal direction visible value
     *
     * @param dir  the dir.
     * @param entityPosition  the entity position.
     * @param boardData  the board data.
     * @param boardSize  the board size.
     * @return the diagonal direction visible value
     */
    public int getDiagonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition,
                                                Map<String, BoardEntity> boardData, int boardSize) {

        int foodEaten = 0;
        EntityPosition currentPosition = entityPosition.move(dir);

        while (currentPosition.isValidPosition(boardSize)) {
            BoardEntity entity = boardData.get(currentPosition.toString());
            if (entity instanceof FoodPoint) {
                // Insect eats the food
                foodEaten += ((FoodPoint) entity).getValue();
            }
            // Move to the next position diagonally
            currentPosition = currentPosition.move(dir);
        }

        return foodEaten;
    }

    @Override
    /**
     *
     * Travel diagonally
     *
     * @param dir  the dir.
     * @param entityPosition  the entity position.
     * @param color  the color.
     * @param boardData  the board data.
     * @param boardSize  the board size.
     * @return int
     */
    public int travelDiagonally(Direction dir, EntityPosition entityPosition, InsectColor color,
                                Map<String, BoardEntity> boardData, int boardSize) {

        int foodEaten = 0;

        // Calculate the next position diagonally
        EntityPosition currentPosition = getPosition().move(dir);

        // Calculate the next position diagonally
        while (currentPosition.isValidPosition(boardSize)) {

            // Retrieve the entity at the current position
            BoardEntity entity = boardData.get(currentPosition.toString());

            // Check if the entity is a FoodPoint
            if (entity instanceof FoodPoint) {
                // Spider eats the food
                foodEaten += ((FoodPoint) entity).getValue();
                // Remove the food from the board
                boardData.remove(entity.getPosition().toString());
            }

            // Check for an insect
            if (entity instanceof Insect && !((Insect) entity).getColor().equals(getColor())) {
                //Remove from the board
                boardData.remove(getPosition().toString());
                return foodEaten;
            }

            // Move to the next position diagonally
            currentPosition = currentPosition.move(dir);
        }

        // Remove the Spider from the board after it has finished its travel
        boardData.remove(getPosition().toString());

        // Update the new position
        setPosition(currentPosition);
        return foodEaten;
    }
}
/**
 * The DiagonalMoving interface defines methods for entities that move diagonally on the board.
 */
interface DiagonalMoving {
    /**
     * Gets the visible value in the diagonal direction.
     *
     * @param dir             The diagonal direction.
     * @param entityPosition  The position of the entity on the board.
     * @param boardData       The map containing board entities.
     * @param boardSize       The size of the game board.
     * @return The visible value in the diagonal direction.
     */
    int getDiagonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition,
                                         Map<String, BoardEntity> boardData, int boardSize);
    /**
     * Travels diagonally and handles interactions with the board.
     *
     * @param dir             The diagonal direction.
     * @param entityPosition  The position of the entity on the board.
     * @param color           The color of the entity.
     * @param boardData       The map containing board entities.
     * @param boardSize       The size of the game board.
     * @return The amount of food eaten during the travel.
     */
    int travelDiagonally(Direction dir, EntityPosition entityPosition, InsectColor color,
                         Map<String, BoardEntity> boardData, int boardSize);
}
/**
 * The OrthogonalMoving interface defines methods for entities that move orthogonally on the board.
 */
interface OrthogonalMoving {
    /**
     * Gets the visible value in the orthogonal direction.
     *
     * @param dir             The orthogonal direction.
     * @param entityPosition  The position of the entity on the board.
     * @param boardData       The map containing board entities.
     * @param boardSize       The size of the game board.
     * @return The visible value in the orthogonal direction.
     */
    // Get the visible value in the orthogonal direction
    int getOrthogonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition,
                                           Map<String, BoardEntity> boardData, int boardSize);
    /**
     * Travels orthogonally and handles interactions with the board.
     *
     * @param dir             The orthogonal direction.
     * @param entityPosition  The position of the entity on the board.
     * @param color           The color of the entity.
     * @param boardData       The map containing board entities.
     * @param boardSize       The size of the game board.
     * @return The amount of food eaten during the travel.
     */
    // Travel orthogonally and handle interactions with the board
    int travelOrthogonally(Direction dir, EntityPosition entityPosition, InsectColor color,
                           Map<String, BoardEntity> boardData, int boardSize);
}
/**
 * The InsectColor enum represents different colors of insects.
 */
enum InsectColor {
    RED("Red"),
    GREEN("Green"),
    BLUE("Blue"),
    YELLOW("Yellow");

    private final String text;
    /**
     * Constructor for InsectColor enum.
     *
     * @param text The text representation of the color.
     */
    InsectColor(String text) {
        this.text = text;
    }
    /**
     * Converts a string to an InsectColor (case-insensitive).
     *
     * @param s The input string representing a color.
     * @return The corresponding InsectColor.
     * @throws InvalidInsectColorException If the input string does not match any color.
     */
    public InsectColor toColor(String s) throws InvalidInsectColorException {
        switch (s.toUpperCase()) {
            case "RED":
                return RED;
            case "GREEN":
                return GREEN;
            case "BLUE":
                return BLUE;
            case "YELLOW":
                return YELLOW;
            default:
                throw new InvalidInsectColorException();
        }
    }
    /**
     * Gets the text representation of the color.
     *
     * @return The text representation of the color.
     */
    // Get the text representation of the color
    public String getTextRepresentation() {
        return text;
    }

}
/**
 * The Type enum represents different types of insects.
 */
enum Type {
    ANT("Ant"),
    BUTTERFLY("Butterfly"),
    SPIDER("Spider"),
    GRASSHOPPER("Grasshopper");

    private final String text;
    /**
     * Constructor for Type enum.
     *
     * @param text The text representation of the insect type.
     */
    Type(String text) {
        this.text = text;
    }
    /**
     * Converts a string to an InsectType.
     *
     * @param s The input string representing an insect type.
     * @return The corresponding InsectType.
     * @throws InvalidInsectTypeException If the input string does not match any insect type.
     */
    public Type toType(String s) throws InvalidInsectTypeException {
        switch (s) {
            case "Ant":
                return ANT;
            case "Butterfly":
                return BUTTERFLY;
            case "Spider":
                return SPIDER;
            case "Grasshopper":
                return GRASSHOPPER;
            default:
                throw new InvalidInsectTypeException();
        }
    }
    /**
     * Gets the text representation of the insect type.
     *
     * @return The text representation of the insect type.
     */
    public String getTextRepresentation() {
        return text;
    }
}
/**
 * The Direction enum represents different directions.
 */
enum Direction {

    N("North"),
    E("East"),
    S("South"),
    W("West"),
    NE("North-East"),
    SE("South-East"),
    SW("South-West"),
    NW("North-West");

    private final String text;
    /**
     * Constructor for Direction enum.
     *
     * @param text The text representation of the direction.
     */
    Direction(String text) {
        this.text = text;
    }
    /**
     * Gets the text representation of the direction.
     *
     * @return The text representation of the direction.
     */
    public String getTextRepresentation() {
        return text;
    }
}
/**
 * The InvalidBoardSizeException class represents an exception for invalid board size.
 */
class InvalidBoardSizeException extends Exception {
    /**
     *
     * Invalid board size exception
     *
     * @return public
     */
    public InvalidBoardSizeException() {
        super();
    }
    /**
     *
     * Invalid board size exception
     *
     * @param message  the message.
     * @return public
     */
    public InvalidBoardSizeException(String message) {
        super(message);
    }

    @Override
    /**
     *
     * Gets the message
     *
     * @return the message
     */
    public String getMessage() {
        return "Invalid board size";
    }
}
/**
 * The InvalidNumberOfInsectsException class represents an exception for an invalid number of insects.
 */
class InvalidNumberOfInsectsException extends Exception {
    /**
     *
     * Invalid number of insects exception
     *
     * @return public
     */
    public InvalidNumberOfInsectsException() {
        super();
    }
    /**
     *
     * Invalid number of insects exception
     *
     * @param message  the message.
     * @return public
     */
    public InvalidNumberOfInsectsException(String message) {
        super(message);
    }

    @Override
    /**
     *
     * Gets the message
     *
     * @return the message
     */
    public String getMessage() {
        return "Invalid number of insects";
    }
}

/**
 * The InvalidNumberOfFoodPointsException class represents an exception for an invalid number of food points.
 */
class InvalidNumberOfFoodPointsException extends Exception {
    /**
     *
     * Invalid number of food points exception
     *
     * @return public
     */
    public InvalidNumberOfFoodPointsException() {
        super();
    }
    /**
     *
     * Invalid number of food points exception
     *
     * @param message  the message.
     * @return public
     */
    public InvalidNumberOfFoodPointsException(String message) {
        super(message);
    }

    @Override
    /**
     *
     * Gets the message
     *
     * @return the message
     */
    public String getMessage() {
        return "Invalid number of food points";
    }
}
/**
 * The InvalidInsectColorException class represents an exception for an invalid insect color.
 */
class InvalidInsectColorException extends Exception {
    /**
     *
     * Invalid insect color exception
     *
     * @return public
     */
    public InvalidInsectColorException() {
        super();
    }
    /**
     *
     * Invalid insect color exception
     *
     * @param message  the message.
     * @return public
     */
    public InvalidInsectColorException(String message) {
        super(message);
    }

    @Override
    /**
     *
     * Gets the message
     *
     * @return the message
     */
    public String getMessage() {
        return "Invalid insect color";
    }
}
/**
 * The InvalidInsectTypeException class represents an exception for an invalid insect type.
 */
class InvalidInsectTypeException extends Exception {
    /**
     *
     * Invalid insect type exception
     *
     * @return public
     */
    public InvalidInsectTypeException() {
        super();
    }
    /**
     *
     * Invalid insect type exception
     *
     * @param message  the message.
     * @return public
     */
    public InvalidInsectTypeException(String message) {
        super(message);
    }

    @Override
    /**
     *
     * Gets the message
     *
     * @return the message
     */
    public String getMessage() {
        return "Invalid insect type";
    }
}
/**
 * The InvalidEntityPositionException class represents an exception for an invalid entity position.
 */
class InvalidEntityPositionException extends Exception {
    /**
     *
     * Invalid entity position exception
     *
     * @return public
     */
    public InvalidEntityPositionException() {
        super();
    }
    /**
     *
     * Invalid entity position exception
     *
     * @param message  the message.
     * @return public
     */
    public InvalidEntityPositionException(String message) {
        super(message);
    }

    @Override
    /**
     *
     * Gets the message
     *
     * @return the message
     */
    public String getMessage() {
        return "Invalid entity position";
    }
}
/**
 * The DuplicateInsectException class represents an exception for duplicate insects at the same position.
 */
class DuplicateInsectException extends Exception {
    /**
     *
     * Duplicate insect exception
     *
     * @return public
     */
    public DuplicateInsectException() {
        super();
    }
    /**
     *
     * Duplicate insect exception
     *
     * @param message  the message.
     * @return public
     */
    public DuplicateInsectException(String message) {
        super(message);
    }

    @Override
    /**
     *
     * Gets the message
     *
     * @return the message
     */
    public String getMessage() {
        return "Duplicate insects";
    }
}

/**
 * The TwoEntitiesOnSamePositionException class represents an exception for two entities occupying the same position.
 */
// Exception class for two entities occupying the same position
class TwoEntitiesOnSamePositionException extends Exception {
    /**
     *
     * Two entities on same position exception
     *
     * @return public
     */
    public TwoEntitiesOnSamePositionException() {
        super();
    }
    /**
     *
     * Two entities on same position exception
     *
     * @param message  the message.
     * @return public
     */
    public TwoEntitiesOnSamePositionException(String message) {
        super(message);
    }

    @Override
    /**
     *
     * Gets the message
     *
     * @return the message
     */
    public String getMessage() {
        return "Two entities in the same position";
    }
}