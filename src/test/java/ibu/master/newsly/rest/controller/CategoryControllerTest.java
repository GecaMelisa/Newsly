package ibu.master.newsly.rest.controller;

import ibu.master.newsly.core.model.Category;
import ibu.master.newsly.core.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    private MockMvc mockMvc;

    @Autowired
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryController = new CategoryController(categoryService);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void testCreateCategory_Success() throws Exception {
        // Given
        Category category = new Category();
        category.setId(1L);
        category.setName("Technology");

        when(categoryService.createCategory(any(Category.class))).thenReturn(category);

        // When & Then
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "Technology"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Technology"));

        verify(categoryService, times(1)).createCategory(any(Category.class));
    }

    @Test
    void testGetAllCategories_Success() throws Exception {
        // Given
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Technology");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Health");

        when(categoryService.getAllCategories()).thenReturn(Arrays.asList(category1, category2));

        // When & Then
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Technology"))
                .andExpect(jsonPath("$[1].name").value("Health"));

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void testGetCategoryById_Success() throws Exception {
        // Given
        Category category = new Category();
        category.setId(1L);
        category.setName("Technology");

        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(category));

        // When & Then
        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Technology"));

        verify(categoryService, times(1)).getCategoryById(1L);
    }

    @Test
    void testGetCategoryById_NotFound() throws Exception {
        // Given
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isNotFound());

        verify(categoryService, times(1)).getCategoryById(1L);
    }

    @Test
    void testUpdateCategory_Success() throws Exception {
        // Given
        Category updatedCategory = new Category();
        updatedCategory.setId(1L);
        updatedCategory.setName("Updated Technology");

        when(categoryService.updateCategory(eq(1L), any(Category.class))).thenReturn(updatedCategory);

        // When & Then
        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "Updated Technology"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Technology"));

        verify(categoryService, times(1)).updateCategory(eq(1L), any(Category.class));
    }

    @Test
    void testDeleteCategory_Success() throws Exception {
        // Given
        doNothing().when(categoryService).deleteCategory(1L);

        // When & Then
        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteCategory(1L);
    }


}
