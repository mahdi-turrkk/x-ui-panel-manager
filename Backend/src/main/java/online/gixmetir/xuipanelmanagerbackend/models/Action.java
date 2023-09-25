package online.gixmetir.xuipanelmanagerbackend.models;

public enum Action {
    CREATE,
    READ,
    UPDATE,
    DELETE;

    public static Action getAction(String name) {
        return switch (name.toLowerCase()) {
            case "create" -> CREATE;
            case "read" -> READ;
            case "update" -> UPDATE;
            case "delete" -> DELETE;
            default -> throw new RuntimeException("Invalid action");
        };
    }

    public static Action getActionFromHttpMethod(String method) {
        return getAction(switch (method.toLowerCase()) {
            case "post":
                yield "create";
            case "get":
                yield "read";
            case "put":
            case "patch":
                yield "update";
            case "delete":
                yield "delete";
            default:
                throw new RuntimeException("Http method is not supported");
        });
    }
}
