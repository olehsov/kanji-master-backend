package com.kanjimaster.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

/**
 * Turns domain exceptions into typed GraphQL errors instead of a generic INTERNAL_ERROR.
 */
@Component
@Log4j2
public class GraphQlExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        ErrorType type;
        String message = ex.getMessage();
        if (ex instanceof NotFoundException) {
            type = ErrorType.NOT_FOUND;
        } else if (ex instanceof BadRequestException) {
            type = ErrorType.BAD_REQUEST;
        } else if (ex instanceof AiGenerationException) {
            log.warn("AI generation failed: {}", ex.getMessage(), ex);
            type = ErrorType.INTERNAL_ERROR;
        } else {
            // Let Spring GraphQL handle unknown exceptions (generic INTERNAL_ERROR, no internals leaked).
            return null;
        }
        return GraphqlErrorBuilder.newError(env)
                .errorType(type)
                .message(message)
                .build();
    }
}
