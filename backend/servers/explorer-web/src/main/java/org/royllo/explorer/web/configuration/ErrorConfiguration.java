package org.royllo.explorer.web.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import static org.royllo.explorer.web.util.constants.PagesConstants.ERROR_404_PAGE;
import static org.royllo.explorer.web.util.constants.PagesConstants.ERROR_500_PAGE;
import static org.royllo.explorer.web.util.constants.PagesConstants.ERROR_PAGE;

/**
 * Error controller.
 */
@Controller
@RequiredArgsConstructor
public class ErrorConfiguration implements ErrorController {

    /**
     * Handle error.
     *
     * @param request request
     * @return page to display
     */
    @RequestMapping("/error")
    public String handleError(final HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return ERROR_404_PAGE;
            }
            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return ERROR_500_PAGE;
            }
        }
        return ERROR_PAGE;
    }

}
