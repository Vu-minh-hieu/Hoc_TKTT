public class Division implements Expression{

    private final Expression leftExpression;
    private final Expression rightExpression;

    public Division(Expression leftExpression,Expression rightExpression ){
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }
    @Override
    public int interpret() {
        return leftExpression.interpret() / rightExpression.interpret();
    }
}