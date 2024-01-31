package org.royllo.explorer.web.configuration;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.util.base.Base;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static jakarta.servlet.RequestDispatcher.ERROR_STATUS_CODE;
import static org.royllo.explorer.web.util.constants.UtilPagesConstants.ERROR_404_PAGE;
import static org.royllo.explorer.web.util.constants.UtilPagesConstants.ERROR_500_PAGE;
import static org.royllo.explorer.web.util.constants.UtilPagesConstants.ERROR_PAGE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Error controller.
 */
@Controller
@RequiredArgsConstructor
public class ErrorConfiguration extends Base implements ErrorController {

    /**
     * Handle error.
     *
     * @param request request
     * @return page to display
     */
    @RequestMapping("/error")
    public String handleError(final HttpServletRequest request) {
        Object status = request.getAttribute(ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == NOT_FOUND.value()) {
                logger.error("Error 404: Page not found: {}", request.getRequestURI());
                return ERROR_404_PAGE;
            }
            if (statusCode == INTERNAL_SERVER_ERROR.value()) {
                return ERROR_500_PAGE;
            }
        }
        return ERROR_PAGE;
    }

}
